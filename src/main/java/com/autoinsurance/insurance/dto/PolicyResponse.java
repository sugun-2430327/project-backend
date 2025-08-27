package com.autoinsurance.insurance.dto;

import com.autoinsurance.insurance.model.Policy;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PolicyResponse {

    private Long policyId;
    private String policyNumber;
    private String vehicleType;
    private BigDecimal coverageAmount;
    private String coverageType;
    private BigDecimal premiumAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String policyStatus;
    private String policyHolderName;


    // Default constructor
    public PolicyResponse() {
    }

    // Constructor from Policy entity
    public PolicyResponse(Policy policy) {
        this.policyId = policy.getPolicyId();
        this.policyNumber = policy.getPolicyNumber();
        this.vehicleType = policy.getVehicleType();
        this.coverageAmount = policy.getCoverageAmount();
        this.coverageType = policy.getCoverageType();
        this.premiumAmount = policy.getPremiumAmount();
        this.startDate = policy.getStartDate();
        this.endDate = policy.getEndDate();
        this.policyStatus = policy.getPolicyStatus().name();
        // Policy templates don't have policyHolder - those are in PolicyEnrollment
        this.policyHolderName = null;
    }

    // Getters and Setters
    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public BigDecimal getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(BigDecimal coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public String getCoverageType() {
        return coverageType;
    }

    public void setCoverageType(String coverageType) {
        this.coverageType = coverageType;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(BigDecimal premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getPolicyStatus() {
        return policyStatus;
    }

    public void setPolicyStatus(String policyStatus) {
        this.policyStatus = policyStatus;
    }

    public String getPolicyHolderName() {
        return policyHolderName;
    }

    public void setPolicyHolderName(String policyHolderName) {
        this.policyHolderName = policyHolderName;
    }


}
