package com.autoinsurance.insurance.service;

import com.autoinsurance.insurance.dto.PolicyRequest;
import com.autoinsurance.insurance.dto.PolicyResponse;
import com.autoinsurance.insurance.exception.AccessDeniedException;
import com.autoinsurance.insurance.model.Policy;
import com.autoinsurance.insurance.model.Role;
import com.autoinsurance.insurance.model.User;
import com.autoinsurance.insurance.repository.PolicyRepository;
import com.autoinsurance.insurance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PolicyService {

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new policy template
     * Only ADMIN can create policy templates
     */
    public PolicyResponse validateCreatePolicy(PolicyRequest request, Principal principal) {
        User currentUser = getCurrentUser(principal);
        
        // Only ADMIN can create policy templates
        if (currentUser.getRole() == Role.CUSTOMER) {
            throw new AccessDeniedException("Customers cannot create policies");
        }
        
        // Reject requests with policy holder assignments
        if (request.getPolicyHolderId() != null) {
            throw new RuntimeException("Cannot assign policy holder during template creation. Use enrollment system instead.");
        }
        
        // Create policy template
        Policy policy = new Policy(
            request.getPolicyNumber(),
            request.getVehicleDetails(),
            request.getCoverageAmount(),
            request.getCoverageType(),
            request.getPremiumAmount(),
            request.getStartDate(),
            request.getEndDate(),
            Policy.PolicyStatus.valueOf(request.getPolicyStatus().toUpperCase())
        );
        
        Policy savedPolicy = policyRepository.save(policy);
        return new PolicyResponse(savedPolicy);
    }

    /**
     * Update an existing policy template
     */
    public PolicyResponse validateUpdatePolicy(Long policyId, PolicyRequest updates, Principal principal) {
        User currentUser = getCurrentUser(principal);
        
        // Only ADMIN can update policy templates
        if (currentUser.getRole() == Role.CUSTOMER) {
            throw new AccessDeniedException("Customers cannot update policy templates");
        }
        
        Policy existingPolicy = policyRepository.findById(policyId)
            .orElseThrow(() -> new RuntimeException("Policy template not found"));
        
        // Update policy template fields
        existingPolicy.setVehicleDetails(updates.getVehicleDetails());
        existingPolicy.setCoverageAmount(updates.getCoverageAmount());
        existingPolicy.setCoverageType(updates.getCoverageType());
        existingPolicy.setPremiumAmount(updates.getPremiumAmount());
        existingPolicy.setStartDate(updates.getStartDate());
        existingPolicy.setEndDate(updates.getEndDate());
        existingPolicy.setPolicyStatus(Policy.PolicyStatus.valueOf(updates.getPolicyStatus().toUpperCase()));
        
        Policy savedPolicy = policyRepository.save(existingPolicy);
        return new PolicyResponse(savedPolicy);
    }

    /**
     * Fetch all policy templates
     */
    public List<PolicyResponse> fetchAllPolicies(Principal principal) {
        List<Policy> policies = policyRepository.findAll();
        
        return policies.stream()
            .map(PolicyResponse::new)
            .collect(Collectors.toList());
    }

    /**
     * Fetch policy template by ID
     */
    public PolicyResponse fetchPolicyById(Long policyId, Principal principal) {
        Policy policy = policyRepository.findById(policyId)
            .orElseThrow(() -> new RuntimeException("Policy template not found"));
        
        return new PolicyResponse(policy);
    }

    /**
     * Delete policy template - ADMIN only
     */
    public void removePolicy(Long policyId, Principal principal) {
        User currentUser = getCurrentUser(principal);
        
        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only admins can delete policy templates");
        }
        
        if (!policyRepository.existsById(policyId)) {
            throw new RuntimeException("Policy template not found");
        }
        
        policyRepository.deleteById(policyId);
    }

    /**
     * Fetch policy template by policy number
     */
    public PolicyResponse fetchPolicyByNumber(String policyNumber, Principal principal) {
        Policy policy = policyRepository.findByPolicyNumber(policyNumber)
            .orElseThrow(() -> new RuntimeException("Policy template not found"));
        
        return new PolicyResponse(policy);
    }

    // ==========================================================================
    // PUBLIC ACCESS METHODS - No authentication required
    // ==========================================================================

    /**
     * Fetch all policy templates - PUBLIC ACCESS
     * Policy templates serve as a product catalog and should be publicly visible
     */
    public List<PolicyResponse> fetchAllPolicyTemplates() {
        List<Policy> policies = policyRepository.findAll();
        
        return policies.stream()
            .map(PolicyResponse::new)
            .collect(Collectors.toList());
    }

    /**
     * Fetch policy template by ID - PUBLIC ACCESS
     * Policy templates serve as a product catalog and should be publicly visible
     */
    public PolicyResponse fetchPolicyTemplateById(Long policyId) {
        Policy policy = policyRepository.findById(policyId)
            .orElseThrow(() -> new RuntimeException("Policy template not found"));
        
        return new PolicyResponse(policy);
    }

    /**
     * Fetch policy template by policy number - PUBLIC ACCESS
     * Policy templates serve as a product catalog and should be publicly visible
     */
    public PolicyResponse fetchPolicyTemplateByNumber(String policyNumber) {
        Policy policy = policyRepository.findByPolicyNumber(policyNumber)
            .orElseThrow(() -> new RuntimeException("Policy template not found"));
        
        return new PolicyResponse(policy);
    }

    /*
     * ==========================================================================
     * ENROLLMENT METHODS MOVED TO PolicyEnrollmentService
     * ==========================================================================
     * 
     * Customer enrollment, admin approval, and decline operations have been
     * moved to PolicyEnrollmentService for better separation of concerns.
     * 
     * This PolicyService now only handles policy template CRUD operations.
     * ==========================================================================
     */

    /**
     * Helper method to get current user from principal
     */
    private User getCurrentUser(Principal principal) {
        return userRepository.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
