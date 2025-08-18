package com.autoinsurance.insurance.controller;

import com.autoinsurance.insurance.dto.SupportTicketRequest;
import com.autoinsurance.insurance.dto.SupportTicketResponse;
import com.autoinsurance.insurance.dto.TicketResolutionRequest;
import com.autoinsurance.insurance.model.TicketStatus;
import com.autoinsurance.insurance.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/support")
public class SupportController {

    @Autowired
    private SupportService supportService;

    /**
     * Create a new support ticket
     * Only customers can create tickets
     */
    @PostMapping("/tickets")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<SupportTicketResponse> createTicket(
            @RequestBody SupportTicketRequest request, 
            Principal principal) {
        
        SupportTicketResponse response = supportService.createTicket(request, principal);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get ticket details by ID
     * - Customers can only view their own tickets
     * - Agents and admins can view any ticket
     */
    @GetMapping("/tickets/{id}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('AGENT') or hasRole('ADMIN')")
    public ResponseEntity<SupportTicketResponse> getTicketDetails(
            @PathVariable Long id, 
            Principal principal) {
        
        SupportTicketResponse response = supportService.getTicketDetails(id, principal);
        return ResponseEntity.ok(response);
    }

    /**
     * Resolve a support ticket
     * Only agents and admins can resolve tickets
     */
    @PutMapping("/tickets/{id}/resolve")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    public ResponseEntity<SupportTicketResponse> resolveTicket(
            @PathVariable Long id, 
            @RequestBody TicketResolutionRequest request, 
            Principal principal) {
        
        SupportTicketResponse response = supportService.resolveTicket(id, request, principal);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all tickets based on user role
     * - Customers see only their own tickets
     * - Agents and admins see all tickets
     */
    @GetMapping("/tickets")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('AGENT') or hasRole('ADMIN')")
    public ResponseEntity<List<SupportTicketResponse>> getAllTickets(Principal principal) {
        
        List<SupportTicketResponse> tickets = supportService.getAllTickets(principal);
        return ResponseEntity.ok(tickets);
    }

    /**
     * Get all open tickets
     * Only agents and admins can access this
     */
    @GetMapping("/tickets/open")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    public ResponseEntity<List<SupportTicketResponse>> getAllOpenTickets(Principal principal) {
        
        List<SupportTicketResponse> openTickets = supportService.getAllOpenTickets(principal);
        return ResponseEntity.ok(openTickets);
    }

    /**
     * Get tickets by status
     * - Customers see only their own tickets with the specified status
     * - Agents and admins see all tickets with the specified status
     */
    @GetMapping("/tickets/status/{status}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('AGENT') or hasRole('ADMIN')")
    public ResponseEntity<List<SupportTicketResponse>> getTicketsByStatus(
            @PathVariable TicketStatus status, 
            Principal principal) {
        
        List<SupportTicketResponse> tickets = supportService.getTicketsByStatus(status, principal);
        return ResponseEntity.ok(tickets);
    }

    /**
     * Get tickets resolved by current agent/admin
     */
    @GetMapping("/tickets/resolved-by-me")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    public ResponseEntity<List<SupportTicketResponse>> getTicketsResolvedByMe(Principal principal) {
        
        List<SupportTicketResponse> tickets = supportService.getTicketsResolvedByMe(principal);
        return ResponseEntity.ok(tickets);
    }

    /**
     * Health check endpoint for the support module
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Customer Support Module is running");
    }
}
