package com.autoinsurance.insurance.repository;

import com.autoinsurance.insurance.model.Policy;
import com.autoinsurance.insurance.model.Policy.PolicyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {

    // Find policy by policy number
    Optional<Policy> findByPolicyNumber(String policyNumber);

    // Find policies by status
    List<Policy> findByPolicyStatus(PolicyStatus policyStatus);

    /*
     * ==========================================================================
     * REMOVED METHODS - Policy entity no longer has policyHolder/agent fields
     * ==========================================================================
     * 
     * The following methods have been removed because the Policy entity has been
     * simplified to be template-only. User relationships are now handled in the
     * PolicyEnrollment table:
     * 
     * - findByPolicyHolder(User policyHolder) - moved to PolicyEnrollmentRepository
     * - findByAgent(User agent) - moved to PolicyEnrollmentRepository  
     * - existsByPolicyIdAndPolicyHolder(...) - no longer needed
     * - existsByPolicyIdAndAgent(...) - no longer needed
     * - findAvailablePolicyTemplates() - use findAll() instead (all are templates)
     * - findEnrolledPolicies() - moved to PolicyEnrollmentRepository
     * ==========================================================================
     */
}
