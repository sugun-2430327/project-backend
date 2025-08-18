package com.autoinsurance.insurance.service;

import com.autoinsurance.insurance.dto.SupportTicketRequest;
import com.autoinsurance.insurance.dto.SupportTicketResponse;
import com.autoinsurance.insurance.dto.TicketResolutionRequest;
import com.autoinsurance.insurance.exception.AccessDeniedException;
import com.autoinsurance.insurance.exception.ResourceNotFoundException;
import com.autoinsurance.insurance.model.*;
import com.autoinsurance.insurance.repository.ClaimRepository;
import com.autoinsurance.insurance.repository.PolicyEnrollmentRepository;
import com.autoinsurance.insurance.repository.SupportTicketRepository;
import com.autoinsurance.insurance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupportService {

    @Autowired
    private SupportTicketRepository supportTicketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PolicyEnrollmentRepository policyEnrollmentRepository;

    @Autowired
    private ClaimRepository claimRepository;

    /**
     * Create a new support ticket (CUSTOMER only)
     */
    public SupportTicketResponse createTicket(SupportTicketRequest request, Principal principal) {
        User currentUser = getCurrentUser(principal);

        // Only customers can create support tickets
        if (currentUser.getRole() != Role.CUSTOMER) {
            throw new AccessDeniedException("Only customers can create support tickets");
        }

        SupportTicket ticket = new SupportTicket();
        ticket.setUser(currentUser);
        ticket.setIssueDescription(request.getIssueDescription());
        ticket.setTicketStatus(TicketStatus.OPEN);
        ticket.setCreatedDate(LocalDate.now());

        // Link to policy enrollment if provided
        if (request.getPolicyEnrollmentId() != null) {
            PolicyEnrollment policyEnrollment = policyEnrollmentRepository.findById(request.getPolicyEnrollmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Policy enrollment not found"));
            
            // Verify the customer owns this policy enrollment
            if (!policyEnrollment.getCustomer().getUserId().equals(currentUser.getUserId())) {
                throw new AccessDeniedException("You can only create tickets for your own policies");
            }
            
            ticket.setPolicyEnrollment(policyEnrollment);
        }

        // Link to claim if provided
        if (request.getClaimId() != null) {
            Claim claim = claimRepository.findById(request.getClaimId())
                    .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
            
            // Verify the customer owns this claim
            if (!claim.getCustomer().getUserId().equals(currentUser.getUserId())) {
                throw new AccessDeniedException("You can only create tickets for your own claims");
            }
            
            ticket.setClaim(claim);
        }

        SupportTicket savedTicket = supportTicketRepository.save(ticket);
        return new SupportTicketResponse(savedTicket);
    }

    /**
     * Get ticket details
     * - CUSTOMER: Can only view their own tickets
     * - AGENT/ADMIN: Can view any ticket
     */
    public SupportTicketResponse getTicketDetails(Long ticketId, Principal principal) {
        User currentUser = getCurrentUser(principal);
        
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Support ticket not found"));

        // Access control: customers can only view their own tickets
        if (currentUser.getRole() == Role.CUSTOMER && 
            !ticket.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("You can only view your own support tickets");
        }

        return new SupportTicketResponse(ticket);
    }

    /**
     * Resolve a support ticket (AGENT/ADMIN only)
     */
    public SupportTicketResponse resolveTicket(Long ticketId, TicketResolutionRequest request, Principal principal) {
        User currentUser = getCurrentUser(principal);

        // Only agents and admins can resolve tickets
        if (currentUser.getRole() == Role.CUSTOMER) {
            throw new AccessDeniedException("Only agents and admins can resolve support tickets");
        }

        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Support ticket not found"));

        // Cannot resolve already resolved tickets
        if (ticket.getTicketStatus() == TicketStatus.RESOLVED) {
            throw new IllegalStateException("Ticket is already resolved");
        }

        ticket.setTicketStatus(TicketStatus.RESOLVED);
        ticket.setResolvedDate(LocalDate.now());
        ticket.setResolution(request.getResolution());
        ticket.setResolvedBy(currentUser);

        SupportTicket savedTicket = supportTicketRepository.save(ticket);
        return new SupportTicketResponse(savedTicket);
    }

    /**
     * Get all tickets based on user role
     * - CUSTOMER: Gets only their own tickets
     * - AGENT: Gets all tickets (to help with customer support)
     * - ADMIN: Gets all tickets
     */
    public List<SupportTicketResponse> getAllTickets(Principal principal) {
        User currentUser = getCurrentUser(principal);

        List<SupportTicket> tickets;

        switch (currentUser.getRole()) {
            case CUSTOMER:
                // Customers see only their own tickets
                tickets = supportTicketRepository.findByUser(currentUser);
                break;
            case AGENT:
            case ADMIN:
                // Agents and admins see all tickets
                tickets = supportTicketRepository.findAllOrderByCreatedDateDesc();
                break;
            default:
                throw new AccessDeniedException("Invalid role for accessing support tickets");
        }

        return tickets.stream()
                .map(SupportTicketResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Get all open tickets (AGENT/ADMIN only)
     */
    public List<SupportTicketResponse> getAllOpenTickets(Principal principal) {
        User currentUser = getCurrentUser(principal);

        // Only agents and admins can view all open tickets
        if (currentUser.getRole() == Role.CUSTOMER) {
            throw new AccessDeniedException("Only agents and admins can view all open tickets");
        }

        List<SupportTicket> openTickets = supportTicketRepository.findAllOpenTickets();
        
        return openTickets.stream()
                .map(SupportTicketResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Get customer's tickets by status
     */
    public List<SupportTicketResponse> getTicketsByStatus(TicketStatus status, Principal principal) {
        User currentUser = getCurrentUser(principal);

        List<SupportTicket> tickets;

        if (currentUser.getRole() == Role.CUSTOMER) {
            // Customers see only their own tickets with the specified status
            tickets = supportTicketRepository.findByUserAndTicketStatus(currentUser, status);
        } else {
            // Agents and admins see all tickets with the specified status
            tickets = supportTicketRepository.findByTicketStatus(status);
        }

        return tickets.stream()
                .map(SupportTicketResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Get tickets resolved by current agent/admin
     */
    public List<SupportTicketResponse> getTicketsResolvedByMe(Principal principal) {
        User currentUser = getCurrentUser(principal);

        // Only agents and admins can have resolved tickets
        if (currentUser.getRole() == Role.CUSTOMER) {
            throw new AccessDeniedException("Customers cannot access resolved ticket statistics");
        }

        List<SupportTicket> resolvedTickets = supportTicketRepository.findByResolvedBy(currentUser);
        
        return resolvedTickets.stream()
                .map(SupportTicketResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Helper method to get current user from principal
     */
    private User getCurrentUser(Principal principal) {
        String username = principal.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}
