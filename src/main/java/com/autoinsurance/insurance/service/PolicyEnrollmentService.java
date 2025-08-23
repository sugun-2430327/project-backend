package com.autoinsurance.insurance.service;

import com.autoinsurance.insurance.dto.EnrollmentEligibilityResponse;
import com.autoinsurance.insurance.dto.PolicyEnrollmentResponse;
import com.autoinsurance.insurance.exception.AccessDeniedException;
import com.autoinsurance.insurance.model.Policy;
import com.autoinsurance.insurance.model.PolicyEnrollment;
import com.autoinsurance.insurance.model.Role;
import com.autoinsurance.insurance.model.User;
import com.autoinsurance.insurance.repository.PolicyEnrollmentRepository;
import com.autoinsurance.insurance.repository.PolicyRepository;
import com.autoinsurance.insurance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PolicyEnrollmentService {

    @Autowired
    private PolicyEnrollmentRepository enrollmentRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Customer enrolls in a policy template
     */
    public PolicyEnrollmentResponse enrollInPolicyTemplate(Long policyTemplateId, Principal principal) {
        User currentUser = getCurrentUser(principal);

        // Only customers can enroll in policies
        if (currentUser.getRole() != Role.CUSTOMER) {
            throw new AccessDeniedException("Only customers can enroll in policies");
        }

        // Get the policy template
        Policy policyTemplate = policyRepository.findById(policyTemplateId)
                .orElseThrow(() -> new RuntimeException("Policy template not found"));

        // Verify it's an active policy template
        if (policyTemplate.getPolicyStatus() != Policy.PolicyStatus.ACTIVE) {
            throw new RuntimeException("This policy template is not active and cannot be enrolled in.");
        }

        // Check if customer already has an approved enrollment for this template
        if (enrollmentRepository.hasApprovedEnrollment(currentUser, policyTemplate)) {
            throw new RuntimeException("You already have an approved policy for this template. Cannot enroll again.");
        }

        // Check if customer already has a pending enrollment for this template
        if (enrollmentRepository.hasPendingEnrollment(currentUser, policyTemplate)) {
            throw new RuntimeException("You already have a pending enrollment for this template. Please wait for approval.");
        }

        // Generate unique policy number for this enrollment
        String generatedPolicyNumber = generateUniquePolicyNumber(policyTemplate.getPolicyNumber());

        // Create enrollment record
        PolicyEnrollment enrollment = new PolicyEnrollment(policyTemplate, currentUser, generatedPolicyNumber);
        PolicyEnrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return new PolicyEnrollmentResponse(savedEnrollment);
    }

    /**
     * Check detailed enrollment eligibility for a customer
     * Returns specific information about why they can or cannot enroll
     */
    public EnrollmentEligibilityResponse checkEnrollmentEligibility(Long policyTemplateId, Principal principal) {
        User currentUser = getCurrentUser(principal);

        // Only customers can check enrollment eligibility
        if (currentUser.getRole() != Role.CUSTOMER) {
            return new EnrollmentEligibilityResponse(false, "Only customers can enroll in policies", null);
        }

        // Get the policy template
        Policy policyTemplate = policyRepository.findById(policyTemplateId)
                .orElseThrow(() -> new RuntimeException("Policy template not found"));

        // Check if policy template is active
        if (policyTemplate.getPolicyStatus() != Policy.PolicyStatus.ACTIVE) {
            return new EnrollmentEligibilityResponse(false, "This policy template is not active", null);
        }

        // Check existing enrollments for blocking statuses
        if (enrollmentRepository.hasApprovedEnrollment(currentUser, policyTemplate)) {
            return new EnrollmentEligibilityResponse(false, "You already have an APPROVED policy for this template. Cannot enroll again.", "APPROVED");
        }

        if (enrollmentRepository.hasPendingEnrollment(currentUser, policyTemplate)) {
            return new EnrollmentEligibilityResponse(false, "You have a PENDING enrollment awaiting admin review. Please wait for approval.", "PENDING");
        }

        // Check if user had previous declined or withdrawn enrollment (these allow re-enrollment)
        Optional<PolicyEnrollment> latestEnrollment = enrollmentRepository.findLatestEnrollmentByCustomerAndTemplate(currentUser, policyTemplate);
        if (latestEnrollment.isPresent()) {
            String status = latestEnrollment.get().getEnrollmentStatus().name();
            if ("DECLINED".equals(status)) {
                return new EnrollmentEligibilityResponse(true, "You can re-enroll (previous enrollment was DECLINED).", "DECLINED");
            } else if ("WITHDRAWN".equals(status)) {
                return new EnrollmentEligibilityResponse(true, "You can re-enroll (previous enrollment was WITHDRAWN).", "WITHDRAWN");
            }
        }

        // No previous enrollments - eligible
        return new EnrollmentEligibilityResponse(true, "You are eligible to enroll in this policy template.", null);
    }

    /**
     * Admin approves customer enrollment
     */
    public PolicyEnrollmentResponse approveEnrollment(Long enrollmentId, String notes, Principal principal) {
        User currentUser = getCurrentUser(principal);

        // Only admins can approve enrollments
        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only admins can approve enrollments");
        }

        PolicyEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        // Check if enrollment is pending admin approval
        if (enrollment.getEnrollmentStatus() != PolicyEnrollment.EnrollmentStatus.PENDING) {
            throw new RuntimeException("Enrollment is not pending admin review");
        }

        // Approve the enrollment
        enrollment.setEnrollmentStatus(PolicyEnrollment.EnrollmentStatus.APPROVED);
        enrollment.setApprovedDate(LocalDateTime.now());
        enrollment.setAdminNotes(notes);

        PolicyEnrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return new PolicyEnrollmentResponse(savedEnrollment);
    }

    /**
     * Admin declines customer enrollment
     */
    public PolicyEnrollmentResponse declineEnrollment(Long enrollmentId, String reason, Principal principal) {
        User currentUser = getCurrentUser(principal);

        // Only admins can decline enrollments
        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only admins can decline enrollments");
        }

        PolicyEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        // Check if enrollment is pending admin decision
        if (enrollment.getEnrollmentStatus() != PolicyEnrollment.EnrollmentStatus.PENDING) {
            throw new RuntimeException("Enrollment is not pending admin review");
        }

        // Decline the enrollment (user can re-enroll later)
        enrollment.setEnrollmentStatus(PolicyEnrollment.EnrollmentStatus.DECLINED);
        enrollment.setDeclinedDate(LocalDateTime.now());
        enrollment.setAdminNotes(reason);

        PolicyEnrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return new PolicyEnrollmentResponse(savedEnrollment);
    }

    /**
     * Get all pending enrollments for admin review
     */
    public List<PolicyEnrollmentResponse> getPendingEnrollments(Principal principal) {
        User currentUser = getCurrentUser(principal);

        // Only admins can view pending enrollments
        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only admins can view pending enrollments");
        }

        List<PolicyEnrollment> pendingEnrollments = enrollmentRepository.findAllPendingEnrollments();

        return pendingEnrollments.stream()
                .map(PolicyEnrollmentResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Get customer's enrollment history
     */
    public List<PolicyEnrollmentResponse> getCustomerEnrollments(Principal principal) {
        User currentUser = getCurrentUser(principal);

        // Only customers can view their own enrollments
        if (currentUser.getRole() != Role.CUSTOMER) {
            throw new AccessDeniedException("Only customers can view their enrollments");
        }

        List<PolicyEnrollment> enrollments = enrollmentRepository.findByCustomer(currentUser);

        return enrollments.stream()
                .map(PolicyEnrollmentResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Check if customer can enroll in a specific template
     */
    public boolean canCustomerEnroll(Long policyTemplateId, Principal principal) {
        User currentUser = getCurrentUser(principal);

        if (currentUser.getRole() != Role.CUSTOMER) {
            return false;
        }

        Policy policyTemplate = policyRepository.findById(policyTemplateId)
                .orElse(null);

        if (policyTemplate == null || policyTemplate.getPolicyStatus() != Policy.PolicyStatus.ACTIVE) {
            return false;
        }

        // Cannot enroll if already approved or pending
        return !enrollmentRepository.hasApprovedEnrollment(currentUser, policyTemplate) 
                && !enrollmentRepository.hasPendingEnrollment(currentUser, policyTemplate);
    }

    /**
     * Get all policy enrollments with comprehensive details (Admin only)
     * Returns all enrollments regardless of status with user-friendly names
     */
    public List<PolicyEnrollmentResponse> getAllEnrollments(Principal principal) {
        User currentUser = getCurrentUser(principal);
        
        // Only admins can view all enrollments
        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only administrators can view all enrollments");
        }
        
        List<PolicyEnrollment> allEnrollments = enrollmentRepository.findAll();
        
        return allEnrollments.stream()
                .map(PolicyEnrollmentResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Helper method to generate unique policy number for enrolled policies
     */
    private String generateUniquePolicyNumber(String templateNumber) {
        return templateNumber + "-" + System.currentTimeMillis();
    }

    /**
     * Helper method to get current user from principal
     */
    private User getCurrentUser(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
