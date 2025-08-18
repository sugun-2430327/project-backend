package com.autoinsurance.insurance.controller;

import com.autoinsurance.insurance.dto.ClaimRequest;
import com.autoinsurance.insurance.dto.ClaimResponse;
import com.autoinsurance.insurance.dto.ClaimUpdateRequest;
import com.autoinsurance.insurance.model.ClaimStatus;
import com.autoinsurance.insurance.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/claims")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    /**
     * Submit a new claim
     * Allowed roles: CUSTOMER
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ClaimResponse> submitClaim(
            @Valid @RequestBody ClaimRequest claimRequest,
            Principal principal) {
        try {
            ClaimResponse response = claimService.submitClaim(claimRequest, principal);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to submit claim: " + e.getMessage());
        }
    }

    /**
     * Get claim details by ID
     * Allowed roles: ADMIN, AGENT, CUSTOMER (with access control)
     */
    @GetMapping("/{claimId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT') or hasRole('CUSTOMER')")
    public ResponseEntity<ClaimResponse> getClaimDetails(
            @PathVariable Long claimId,
            Principal principal) {
        try {
            ClaimResponse response = claimService.getClaimDetails(claimId, principal);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get claim details: " + e.getMessage());
        }
    }

    /**
     * Update claim status
     * Allowed roles: ADMIN, AGENT
     */
    @PutMapping("/{claimId}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<ClaimResponse> updateClaimStatus(
            @PathVariable Long claimId,
            @Valid @RequestBody ClaimUpdateRequest updateRequest,
            Principal principal) {
        try {
            ClaimResponse response = claimService.updateClaimStatus(claimId, updateRequest, principal);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update claim status: " + e.getMessage());
        }
    }

    /**
     * Get all claims (with role-based filtering)
     * Allowed roles: ADMIN, AGENT, CUSTOMER
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT') or hasRole('CUSTOMER')")
    public ResponseEntity<List<ClaimResponse>> getAllClaims(Principal principal) {
        try {
            List<ClaimResponse> response = claimService.getAllClaims(principal);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get claims: " + e.getMessage());
        }
    }

    /**
     * Get claims by status
     * Allowed roles: ADMIN, AGENT
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<List<ClaimResponse>> getClaimsByStatus(
            @PathVariable ClaimStatus status,
            Principal principal) {
        try {
            List<ClaimResponse> response = claimService.getClaimsByStatus(status, principal);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get claims by status: " + e.getMessage());
        }
    }

    /**
     * Get customer's claims
     * Allowed roles: CUSTOMER, AGENT, ADMIN
     */
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT') or (hasRole('CUSTOMER') and #customerId == authentication.principal.userId)")
    public ResponseEntity<List<ClaimResponse>> getCustomerClaims(
            @PathVariable Long customerId,
            Principal principal) {
        try {
            // This will be handled by the getAllClaims method with role-based filtering
            List<ClaimResponse> response = claimService.getAllClaims(principal);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get customer claims: " + e.getMessage());
        }
    }
}
