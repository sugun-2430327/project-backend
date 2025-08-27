package com.autoinsurance.insurance.controller;

import com.autoinsurance.insurance.dto.EnrollmentEligibilityResponse;
import com.autoinsurance.insurance.dto.EnrollmentRequest;
import com.autoinsurance.insurance.dto.PolicyEnrollmentResponse;
import com.autoinsurance.insurance.service.PolicyEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4200"})
@RestController
@RequestMapping("/api/enrollments")
public class PolicyEnrollmentController {

    @Autowired
    private PolicyEnrollmentService enrollmentService;

    /**
     * Customer enrolls in a policy template
     * Accepts optional vehicle details in request body
     * Allowed roles: CUSTOMER only
     */
    @PostMapping("/{policyTemplateId}/enroll")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PolicyEnrollmentResponse> enrollInPolicyTemplate(
            @PathVariable Long policyTemplateId,
            @RequestBody(required = false) EnrollmentRequest enrollmentRequest,
            Principal principal) {
        try {
            String vehicleDetails = (enrollmentRequest != null) ? enrollmentRequest.getVehicleDetails() : null;
            PolicyEnrollmentResponse enrollment = enrollmentService.enrollInPolicyTemplate(policyTemplateId, vehicleDetails, principal);
            return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to enroll in policy template: " + e.getMessage());
        }
    }

    /**
     * Get all pending enrollments for admin approval
     * Allowed roles: ADMIN only
     */
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PolicyEnrollmentResponse>> getPendingEnrollments(Principal principal) {
        try {
            List<PolicyEnrollmentResponse> pendingEnrollments = enrollmentService.getPendingEnrollments(principal);
            return ResponseEntity.ok(pendingEnrollments);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch pending enrollments: " + e.getMessage());
        }
    }

    /**
     * Get customer's enrollment history
     * Allowed roles: CUSTOMER only
     */
    @GetMapping("/my-enrollments")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<PolicyEnrollmentResponse>> getMyEnrollments(Principal principal) {
        try {
            List<PolicyEnrollmentResponse> enrollments = enrollmentService.getCustomerEnrollments(principal);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch your enrollments: " + e.getMessage());
        }
    }

    /**
     * Check detailed enrollment eligibility for a specific template
     * Returns detailed information about enrollment status and eligibility
     * Allowed roles: CUSTOMER only
     */
    @GetMapping("/{policyTemplateId}/eligibility")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<EnrollmentEligibilityResponse> checkEnrollmentEligibility(
            @PathVariable Long policyTemplateId,
            Principal principal) {
        try {
            EnrollmentEligibilityResponse eligibility = enrollmentService.checkEnrollmentEligibility(policyTemplateId, principal);
            return ResponseEntity.ok(eligibility);
        } catch (Exception e) {
            throw new RuntimeException("Failed to check enrollment eligibility: " + e.getMessage());
        }
    }

    /**
     * Check if customer can enroll in a specific template (simple boolean response)
     * Allowed roles: CUSTOMER only
     */
    @GetMapping("/{policyTemplateId}/can-enroll")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Boolean> canEnrollInTemplate(
            @PathVariable Long policyTemplateId,
            Principal principal) {
        try {
            EnrollmentEligibilityResponse eligibility = enrollmentService.checkEnrollmentEligibility(policyTemplateId, principal);
            return ResponseEntity.ok(eligibility.isCanEnroll());
        } catch (Exception e) {
            throw new RuntimeException("Failed to check enrollment eligibility: " + e.getMessage());
        }
    }

    /**
     * Admin approves customer enrollment
     * Allowed roles: ADMIN only
     */
    @PutMapping("/{enrollmentId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PolicyEnrollmentResponse> approveEnrollment(
            @PathVariable Long enrollmentId,
            @RequestBody(required = false) String notes,
            Principal principal) {
        try {
            PolicyEnrollmentResponse approvedEnrollment = enrollmentService.approveEnrollment(
                enrollmentId, 
                notes, 
                principal
            );
            return ResponseEntity.ok(approvedEnrollment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to approve enrollment: " + e.getMessage());
        }
    }

    /**
     * Admin declines customer enrollment
     * Allowed roles: ADMIN only
     */
    @PutMapping("/{enrollmentId}/decline")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PolicyEnrollmentResponse> declineEnrollment(
            @PathVariable Long enrollmentId,
            @RequestBody(required = false) String reason,
            Principal principal) {
        try {
            PolicyEnrollmentResponse declinedEnrollment = enrollmentService.declineEnrollment(enrollmentId, reason, principal);
            return ResponseEntity.ok(declinedEnrollment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decline enrollment: " + e.getMessage());
        }
    }

    /**
     * Get all policy enrollments with comprehensive details (Admin only)
     * Includes customer names and policy template details
     * Allowed roles: ADMIN only
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PolicyEnrollmentResponse>> getAllEnrollments(Principal principal) {
        try {
            List<PolicyEnrollmentResponse> allEnrollments = enrollmentService.getAllEnrollments(principal);
            return ResponseEntity.ok(allEnrollments);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch all enrollments: " + e.getMessage());
        }
    }

    /**
     * Customer withdraws their own pending enrollment
     * Allowed roles: CUSTOMER only
     */
    @PutMapping("/{enrollmentId}/withdraw")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> withdrawEnrollment(
            @PathVariable Long enrollmentId,
            Principal principal) {
        // This could be implemented if needed - for now return not implemented
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
            .body("Enrollment withdrawal feature coming soon");
    }
}
