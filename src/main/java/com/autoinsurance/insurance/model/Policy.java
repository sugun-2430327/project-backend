package com.autoinsurance.insurance.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "policies")
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long policyId;

    @Column(nullable = false, length = 50)
    private String policyNumber;

    @Column(name = "vehicle_type", columnDefinition = "TEXT")
    private String vehicleType;

    @Column(precision = 10, scale = 2)
    private BigDecimal coverageAmount;

    @Column(length = 100)
    private String coverageType;

    @Column(precision = 10, scale = 2)
    private BigDecimal premiumAmount;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private PolicyStatus policyStatus;

    // Default constructor
    public Policy() {
    }

    // Constructor
    public Policy(String policyNumber, String vehicleType, BigDecimal coverageAmount, String coverageType,
                  BigDecimal premiumAmount, LocalDate startDate, LocalDate endDate, PolicyStatus policyStatus) {
        this.policyNumber = policyNumber;
        this.vehicleType = vehicleType;
        this.coverageAmount = coverageAmount;
        this.coverageType = coverageType;
        this.premiumAmount = premiumAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.policyStatus = policyStatus;
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

    public PolicyStatus getPolicyStatus() {
        return policyStatus;
    }

    public void setPolicyStatus(PolicyStatus policyStatus) {
        this.policyStatus = policyStatus;
    }

    // Policy holder relationships moved to PolicyEnrollment table

    public enum PolicyStatus {
        ACTIVE,      // Policy template is active and available for enrollment
        INACTIVE,    // Policy template is temporarily disabled
        RENEWED      // Policy template has been updated/renewed
    }
}
