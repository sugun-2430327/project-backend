package com.autoinsurance.insurance.controller;

import com.autoinsurance.insurance.dto.PolicyRequest;
import com.autoinsurance.insurance.dto.PolicyResponse;
import com.autoinsurance.insurance.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    /**
     * Create a new policy
     * Allowed roles: ADMIN
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PolicyResponse> createPolicy(
            @Valid @RequestBody PolicyRequest policyRequest,
            Principal principal) {
        try {
            PolicyResponse response = policyService.validateCreatePolicy(policyRequest, principal);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create policy: " + e.getMessage());
        }
    }

    /**
     * Update an existing policy
     * Allowed roles: ADMIN
     */
    @PutMapping("/{policyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PolicyResponse> updatePolicy(
            @PathVariable Long policyId,
            @Valid @RequestBody PolicyRequest policyRequest,
            Principal principal) {
        try {
            PolicyResponse response = policyService.validateUpdatePolicy(policyId, policyRequest, principal);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update policy: " + e.getMessage());
        }
    }

    /**
     * Get all policies based on user role (PROTECTED)
     * ADMIN: returns all policies

     * CUSTOMER: returns only the user's own policies
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<List<PolicyResponse>> getAllPolicies(Principal principal) {
        try {
            List<PolicyResponse> policies = policyService.fetchAllPolicies(principal);
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch policies: " + e.getMessage());
        }
    }

    /**
     * Get policy by ID (PROTECTED)
     * ADMIN: can access any policy

     * CUSTOMER: only if it's their own policy
     */
    @GetMapping("/{policyId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<PolicyResponse> getPolicyById(
            @PathVariable Long policyId,
            Principal principal) {
        try {
            PolicyResponse policy = policyService.fetchPolicyById(policyId, principal);
            return ResponseEntity.ok(policy);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch policy: " + e.getMessage());
        }
    }

    /**
     * Delete a policy
     * Allowed roles: ADMIN only
     */
    @DeleteMapping("/{policyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePolicy(
            @PathVariable Long policyId,
            Principal principal) {
        try {
            policyService.removePolicy(policyId, principal);
            return ResponseEntity.ok("Policy deleted successfully");
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete policy: " + e.getMessage());
        }
    }

    /**
     * Get policy by policy number (PROTECTED)
     * Access control is same as getPolicyById
     */
    @GetMapping("/number/{policyNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<PolicyResponse> getPolicyByNumber(
            @PathVariable String policyNumber,
            Principal principal) {
        try {
            PolicyResponse policy = policyService.fetchPolicyByNumber(policyNumber, principal);
            return ResponseEntity.ok(policy);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch policy by number: " + e.getMessage());
        }
    }

    // ==========================================================================
    // PUBLIC ACCESS ENDPOINTS - No authentication required
    // ==========================================================================

    /**
     * Get all policy templates - PUBLIC ACCESS
     * Policy templates serve as a product catalog and should be publicly visible
     */
    @GetMapping("/public")
    public ResponseEntity<List<PolicyResponse>> getAllPolicyTemplatesPublic() {
        try {
            List<PolicyResponse> policies = policyService.fetchAllPolicyTemplates();
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch policy templates: " + e.getMessage());
        }
    }

    /**
     * Get policy template by ID - PUBLIC ACCESS
     * Policy templates serve as a product catalog and should be publicly visible
     */
    @GetMapping("/public/{policyId}")
    public ResponseEntity<PolicyResponse> getPolicyTemplateByIdPublic(@PathVariable Long policyId) {
        try {
            PolicyResponse policy = policyService.fetchPolicyTemplateById(policyId);
            return ResponseEntity.ok(policy);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch policy template: " + e.getMessage());
        }
    }

    /**
     * Get policy template by policy number - PUBLIC ACCESS
     * Policy templates serve as a product catalog and should be publicly visible
     */
    @GetMapping("/public/number/{policyNumber}")
    public ResponseEntity<PolicyResponse> getPolicyTemplateByNumberPublic(@PathVariable String policyNumber) {
        try {
            PolicyResponse policy = policyService.fetchPolicyTemplateByNumber(policyNumber);
            return ResponseEntity.ok(policy);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch policy template: " + e.getMessage());
        }
    }

    /**
     * @deprecated Use PolicyEnrollmentController for enrollment operations
     * This endpoint is kept for backward compatibility but redirects to new enrollment system
     */
    @PostMapping("/{policyTemplateId}/enroll")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Deprecated
    public ResponseEntity<?> enrollInPolicy(
            @PathVariable Long policyTemplateId,
            Principal principal) {
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
            .header("Location", "/api/enrollments/" + policyTemplateId + "/enroll")
            .body("This endpoint has moved. Please use /api/enrollments/{templateId}/enroll");
    }

    /**
     * @deprecated Use PolicyEnrollmentController for enrollment management
     * This endpoint is kept for backward compatibility
     */
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    @Deprecated
    public ResponseEntity<?> getPendingPolicies(Principal principal) {
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
            .header("Location", "/api/enrollments/pending")
            .body("This endpoint has moved. Please use /api/enrollments/pending");
    }



    /**
     * @deprecated Use PolicyEnrollmentController for decline operations
     * This endpoint is kept for backward compatibility
     */
    @PutMapping("/{policyId}/decline")
    @PreAuthorize("hasRole('ADMIN')")
    @Deprecated
    public ResponseEntity<?> declinePolicy(
            @PathVariable Long policyId,
            @RequestBody(required = false) String reason,
            Principal principal) {
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
            .header("Location", "/api/enrollments/" + policyId + "/decline")
            .body("This endpoint has moved. Please use /api/enrollments/{enrollmentId}/decline");
    }
}
