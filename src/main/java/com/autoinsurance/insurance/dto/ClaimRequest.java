package com.autoinsurance.insurance.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class ClaimRequest {

    @NotNull(message = "Policy enrollment ID is required")
    private Long policyEnrollmentId;

    @NotNull(message = "Claim amount is required")
    @Positive(message = "Claim amount must be positive")
    private BigDecimal claimAmount;

    @Size(max = 1000, message = "Claim description cannot exceed 1000 characters")
    private String claimDescription;

    // Default constructor
    public ClaimRequest() {
    }

    // Constructor
    public ClaimRequest(Long policyEnrollmentId, BigDecimal claimAmount, String claimDescription) {
        this.policyEnrollmentId = policyEnrollmentId;
        this.claimAmount = claimAmount;
        this.claimDescription = claimDescription;
    }

    // Getters and Setters
    public Long getPolicyEnrollmentId() {
        return policyEnrollmentId;
    }

    public void setPolicyEnrollmentId(Long policyEnrollmentId) {
        this.policyEnrollmentId = policyEnrollmentId;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getClaimDescription() {
        return claimDescription;
    }

    public void setClaimDescription(String claimDescription) {
        this.claimDescription = claimDescription;
    }
}
