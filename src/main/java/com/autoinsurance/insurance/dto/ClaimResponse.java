package com.autoinsurance.insurance.dto;

import com.autoinsurance.insurance.model.ClaimStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ClaimResponse {

    private Long claimId;
    private Long policyEnrollmentId;
    private String generatedPolicyNumber;
    private String customerUsername;
    private String customerEmail;
    private String firstName;
    private String lastName;
    private BigDecimal claimAmount;
    private LocalDate claimDate;
    private ClaimStatus claimStatus;
    private String adminUsername;
    private String claimDescription;
    private String adminNotes;

    // Default constructor
    public ClaimResponse() {
    }

    // Constructor
    public ClaimResponse(Long claimId, Long policyEnrollmentId, String generatedPolicyNumber, 
                        String customerUsername, String customerEmail, String firstName, String lastName, 
                        BigDecimal claimAmount, LocalDate claimDate, ClaimStatus claimStatus, String adminUsername, 
                        String claimDescription, String adminNotes) {
        this.claimId = claimId;
        this.policyEnrollmentId = policyEnrollmentId;
        this.generatedPolicyNumber = generatedPolicyNumber;
        this.customerUsername = customerUsername;
        this.customerEmail = customerEmail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.claimAmount = claimAmount;
        this.claimDate = claimDate;
        this.claimStatus = claimStatus;
        this.adminUsername = adminUsername;
        this.claimDescription = claimDescription;
        this.adminNotes = adminNotes;
    }

    // Getters and Setters
    public Long getClaimId() {
        return claimId;
    }

    public void setClaimId(Long claimId) {
        this.claimId = claimId;
    }

    public Long getPolicyEnrollmentId() {
        return policyEnrollmentId;
    }

    public void setPolicyEnrollmentId(Long policyEnrollmentId) {
        this.policyEnrollmentId = policyEnrollmentId;
    }

    public String getGeneratedPolicyNumber() {
        return generatedPolicyNumber;
    }

    public void setGeneratedPolicyNumber(String generatedPolicyNumber) {
        this.generatedPolicyNumber = generatedPolicyNumber;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
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
