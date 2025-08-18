package com.autoinsurance.insurance.service;

import com.autoinsurance.insurance.dto.AgentDashboardResponse;
import com.autoinsurance.insurance.exception.AccessDeniedException;
import com.autoinsurance.insurance.exception.ResourceNotFoundException;
import com.autoinsurance.insurance.model.*;
import com.autoinsurance.insurance.repository.PolicyEnrollmentRepository;
import com.autoinsurance.insurance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgentService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PolicyEnrollmentRepository policyEnrollmentRepository;
    
    @Autowired
    private UserService userService;

    public AgentDashboardResponse getAgentDashboard(String agentIdOrUsername, Principal principal) {
        // Convert string parameter to Long ID
        Long agentId = convertToUserId(agentIdOrUsername);
        // Find the agent by ID
        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found with ID: " + agentId));
        
        // Check if the agent actually has AGENT role
        if (agent.getRole() != Role.AGENT) {
            throw new AccessDeniedException("User is not an agent");
        }
        
        // Get current user details from principal
        String currentUsername = principal.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        
        // Access control: Only the agent themselves or an admin can access this dashboard
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwner = currentUser.getUserId().equals(agentId);
        
        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Access denied. You can only view your own dashboard.");
        }
        
        // Get all policy enrollments assigned to this agent
        List<PolicyEnrollment> assignedEnrollments = policyEnrollmentRepository.findByAgent(agent);
        
        // Convert to DTO
        List<AgentDashboardResponse.AssignedPolicyEnrollment> assignedPolicies = 
                assignedEnrollments.stream()
                        .map(this::convertToAssignedPolicyEnrollmentDTO)
                        .collect(Collectors.toList());
        
        return new AgentDashboardResponse(
                agent.getUserId(),
                agent.getUsername(), // Using username as name since firstName/lastName not available
                agent.getEmail(),
                assignedPolicies
        );
    }
    
    private AgentDashboardResponse.AssignedPolicyEnrollment convertToAssignedPolicyEnrollmentDTO(PolicyEnrollment enrollment) {
        AgentDashboardResponse.AssignedPolicyEnrollment dto = new AgentDashboardResponse.AssignedPolicyEnrollment();
        
        // Enrollment details
        dto.setEnrollmentId(enrollment.getEnrollmentId());
        dto.setGeneratedPolicyNumber(enrollment.getGeneratedPolicyNumber());
        dto.setEnrollmentStatus(enrollment.getEnrollmentStatus().toString());
        dto.setEnrolledDate(enrollment.getEnrolledDate());
        dto.setApprovedDate(enrollment.getApprovedDate());
        dto.setAdminNotes(enrollment.getAdminNotes());
        
        // Customer details
        User customer = enrollment.getCustomer();
        dto.setCustomerId(customer.getUserId());
        dto.setCustomerName(customer.getUsername()); // Using username as name since firstName/lastName not available
        dto.setCustomerEmail(customer.getEmail());
        dto.setCustomerPhone(null); // phoneNumber not available in current User model
        
        // Policy template details
        Policy policyTemplate = enrollment.getPolicyTemplate();
        dto.setPolicyTemplateId(policyTemplate.getPolicyId());
        dto.setPolicyNumber(policyTemplate.getPolicyNumber());
        dto.setVehicleDetails(policyTemplate.getVehicleDetails());
        dto.setCoverageAmount(policyTemplate.getCoverageAmount());
        dto.setCoverageType(policyTemplate.getCoverageType());
        dto.setPremiumAmount(policyTemplate.getPremiumAmount());
        dto.setStartDate(policyTemplate.getStartDate());
        dto.setEndDate(policyTemplate.getEndDate());
        dto.setPolicyStatus(policyTemplate.getPolicyStatus().toString());
        
        return dto;
    }
    
    private Long convertToUserId(String agentIdOrUsername) {
        // First try to parse as numeric ID
        try {
            return Long.parseLong(agentIdOrUsername);
        } catch (NumberFormatException e) {
            // If not numeric, treat as username and lookup ID
            Long userId = userService.findUserIdByUsername(agentIdOrUsername);
            if (userId == null) {
                throw new ResourceNotFoundException("User not found with username: " + agentIdOrUsername);
            }
            return userId;
        }
    }
}
