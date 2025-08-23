package com.autoinsurance.insurance.repository;

import com.autoinsurance.insurance.model.Claim;
import com.autoinsurance.insurance.model.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {

    // Find claims by customer (direct customer field)
    @Query("SELECT c FROM Claim c WHERE c.customer.userId = :customerId")
    List<Claim> findByCustomerId(@Param("customerId") Long customerId);

    // Find claims by status
    List<Claim> findByClaimStatus(ClaimStatus claimStatus);

    // Find claims by policy enrollment ID
    @Query("SELECT c FROM Claim c WHERE c.policyEnrollment.enrollmentId = :enrollmentId")
    List<Claim> findByPolicyEnrollmentId(@Param("enrollmentId") Long enrollmentId);



    // Count claims by status
    long countByClaimStatus(ClaimStatus claimStatus);

    // Find recent claims (last 30 days)
    @Query("SELECT c FROM Claim c WHERE c.claimDate >= CURRENT_DATE - 30 DAY")
    List<Claim> findRecentClaims();
}
