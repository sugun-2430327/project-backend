package com.autoinsurance.insurance.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long claimId;

    // Foreign key to PolicyEnrollment (customer's enrolled policy)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_enrollment_id", nullable = false)
    private PolicyEnrollment policyEnrollment;

    // Foreign key to Policy template (for direct policy reference)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    // Foreign key to Customer (for direct customer tracking)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal claimAmount;

    @Column(nullable = false)
    private LocalDate claimDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus claimStatus;

    @Column(columnDefinition = "TEXT")
    private String claimDescription;

    @Column(columnDefinition = "TEXT")
    private String adminNotes;

    // Default constructor
    public Claim() {
        this.claimDate = LocalDate.now();
        this.claimStatus = ClaimStatus.OPEN;
    }

    // Constructor
    public Claim(PolicyEnrollment policyEnrollment, BigDecimal claimAmount, String claimDescription) {
        this();
        this.policyEnrollment = policyEnrollment;
        this.policy = policyEnrollment.getPolicyTemplate(); // Set policy from enrollment
        this.customer = policyEnrollment.getCustomer(); // Set customer from enrollment
        this.claimAmount = claimAmount;
        this.claimDescription = claimDescription;
    }

    // Getters and Setters
    public Long getClaimId() {
        return claimId;
    }

    public void setClaimId(Long claimId) {
        this.claimId = claimId;
    }

    public PolicyEnrollment getPolicyEnrollment() {
        return policyEnrollment;
    }

    public void setPolicyEnrollment(PolicyEnrollment policyEnrollment) {
        this.policyEnrollment = policyEnrollment;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
    }

    public ClaimStatus getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(ClaimStatus claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getClaimDescription() {
        return claimDescription;
    }

    public void setClaimDescription(String claimDescription) {
        this.claimDescription = claimDescription;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }
}
