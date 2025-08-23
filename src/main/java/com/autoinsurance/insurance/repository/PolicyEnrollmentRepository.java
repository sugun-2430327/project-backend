package com.autoinsurance.insurance.repository;

import com.autoinsurance.insurance.model.Policy;
import com.autoinsurance.insurance.model.PolicyEnrollment;
import com.autoinsurance.insurance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyEnrollmentRepository extends JpaRepository<PolicyEnrollment, Long> {

    // Find enrollments by customer
    List<PolicyEnrollment> findByCustomer(User customer);

    // Find enrollments by status
    List<PolicyEnrollment> findByEnrollmentStatus(PolicyEnrollment.EnrollmentStatus status);



    // Check if customer already has an approved enrollment for a policy template
    @Query("SELECT COUNT(pe) > 0 FROM PolicyEnrollment pe WHERE pe.customer = :customer AND pe.policyTemplate = :policyTemplate AND pe.enrollmentStatus = 'APPROVED'")
    boolean hasApprovedEnrollment(@Param("customer") User customer, @Param("policyTemplate") Policy policyTemplate);

    // Check if customer has a pending enrollment for a policy template
    @Query("SELECT COUNT(pe) > 0 FROM PolicyEnrollment pe WHERE pe.customer = :customer AND pe.policyTemplate = :policyTemplate AND pe.enrollmentStatus = 'PENDING'")
    boolean hasPendingEnrollment(@Param("customer") User customer, @Param("policyTemplate") Policy policyTemplate);

    // Find customer's enrollment for a specific policy template
    @Query("SELECT pe FROM PolicyEnrollment pe WHERE pe.customer = :customer AND pe.policyTemplate = :policyTemplate ORDER BY pe.enrolledDate DESC")
    List<PolicyEnrollment> findByCustomerAndPolicyTemplate(@Param("customer") User customer, @Param("policyTemplate") Policy policyTemplate);

    // Find the latest enrollment for a customer and policy template
    @Query("SELECT pe FROM PolicyEnrollment pe WHERE pe.customer = :customer AND pe.policyTemplate = :policyTemplate ORDER BY pe.enrolledDate DESC LIMIT 1")
    Optional<PolicyEnrollment> findLatestEnrollmentByCustomerAndTemplate(@Param("customer") User customer, @Param("policyTemplate") Policy policyTemplate);

    // Find all enrollments pending admin review (newly submitted)
    @Query("SELECT pe FROM PolicyEnrollment pe WHERE pe.enrollmentStatus = 'PENDING' ORDER BY pe.enrolledDate ASC")
    List<PolicyEnrollment> findAllPendingEnrollments();






}
