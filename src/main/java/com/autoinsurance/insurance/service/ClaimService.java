package com.autoinsurance.insurance.service;

import com.autoinsurance.insurance.dto.ClaimRequest;
import com.autoinsurance.insurance.dto.ClaimResponse;
import com.autoinsurance.insurance.dto.ClaimUpdateRequest;
import com.autoinsurance.insurance.exception.AccessDeniedException;
import com.autoinsurance.insurance.exception.ResourceNotFoundException;
import com.autoinsurance.insurance.model.*;
import com.autoinsurance.insurance.model.PolicyEnrollment.EnrollmentStatus;
import com.autoinsurance.insurance.repository.ClaimRepository;
import com.autoinsurance.insurance.repository.PolicyEnrollmentRepository;
import com.autoinsurance.insurance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClaimService {

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private PolicyEnrollmentRepository policyEnrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Submit a new claim (CUSTOMER only)
     */
    public ClaimResponse submitClaim(ClaimRequest claimRequest, Principal principal) {
        User currentUser = getCurrentUser(principal);

        // Only customers can submit claims
        if (currentUser.getRole() != Role.CUSTOMER) {
            throw new AccessDeniedException("Only customers can submit claims");
        }

        // Get the policy enrollment
        PolicyEnrollment policyEnrollment = policyEnrollmentRepository.findById(claimRequest.getPolicyEnrollmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy enrollment not found"));

        // Verify the customer owns this policy enrollment
        if (!policyEnrollment.getCustomer().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("You can only submit claims for your own policies");
        }

        // Verify the policy enrollment is approved
        if (policyEnrollment.getEnrollmentStatus() != EnrollmentStatus.APPROVED) {
            throw new AccessDeniedException("You can only submit claims for approved policy enrollments");
        }

        // Create and save the claim
        Claim claim = new Claim(policyEnrollment, claimRequest.getClaimAmount(), claimRequest.getClaimDescription());
        claim = claimRepository.save(claim);

        return convertToResponse(claim);
    }

    /**
     * Get claim details by ID with role-based access control
     */
    public ClaimResponse getClaimDetails(Long claimId, Principal principal) {
        User currentUser = getCurrentUser(principal);
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        // Access control based on role
        switch (currentUser.getRole()) {
            case ADMIN:
                // Admin can see all claims
                break;
            case CUSTOMER:
                // Customer can only see their own claims
                if (!claim.getCustomer().getUserId().equals(currentUser.getUserId())) {
                    throw new AccessDeniedException("You can only view your own claims");
                }
                break;
            default:
                throw new AccessDeniedException("Invalid role for accessing claims");
        }

        return convertToResponse(claim);
    }

    /**
     * Update claim status (ADMIN only)
     */
    public ClaimResponse updateClaimStatus(Long claimId, ClaimUpdateRequest updateRequest, Principal principal) {
        User currentUser = getCurrentUser(principal);
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        // Only ADMIN can update claim status
        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only admins can update claim status");
        }

        // Update claim details
        claim.setClaimStatus(updateRequest.getClaimStatus());
        claim.setAdminNotes(updateRequest.getAdminNotes());

        claim = claimRepository.save(claim);
        return convertToResponse(claim);
    }

    /**
     * Get all claims with role-based filtering
     */
    public List<ClaimResponse> getAllClaims(Principal principal) {
        User currentUser = getCurrentUser(principal);
        List<Claim> claims;

        switch (currentUser.getRole()) {
            case ADMIN:
                // Admin can see all claims
                claims = claimRepository.findAll();
                break;
            case CUSTOMER:
                // Customer can only see their own claims
                claims = claimRepository.findByCustomerId(currentUser.getUserId());
                break;
            default:
                throw new AccessDeniedException("Invalid role for accessing claims");
        }

        return claims.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get claims by status (ADMIN only)
     */
    public List<ClaimResponse> getClaimsByStatus(ClaimStatus status, Principal principal) {
        User currentUser = getCurrentUser(principal);

        // Only ADMIN can filter by status
        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only admins can filter claims by status");
        }

        List<Claim> claims = claimRepository.findByClaimStatus(status);

        return claims.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convert Claim entity to ClaimResponse DTO
     */
    private ClaimResponse convertToResponse(Claim claim) {
        return new ClaimResponse(
                claim.getClaimId(),
                claim.getPolicyEnrollment().getEnrollmentId(),
                claim.getPolicyEnrollment().getGeneratedPolicyNumber(),
                claim.getPolicyEnrollment().getCustomer().getUsername(),
                claim.getPolicyEnrollment().getCustomer().getEmail(),
                claim.getClaimAmount(),
                claim.getClaimDate(),
                claim.getClaimStatus(),
                null, // No agent assigned anymore
                claim.getClaimDescription(),
                claim.getAdminNotes()
        );
    }

    /**
     * Get current user from Principal
     */
    private User getCurrentUser(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
