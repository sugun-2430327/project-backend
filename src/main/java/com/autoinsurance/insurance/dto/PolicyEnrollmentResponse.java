package com.autoinsurance.insurance.dto;

import com.autoinsurance.insurance.model.PolicyEnrollment;
import java.time.LocalDateTime;

public class PolicyEnrollmentResponse {

    private Long enrollmentId;
    private Long policyTemplateId;
    private String policyTemplateNumber;
    private String customerName;
    private String customerEmail;
    private String agentName;
    private String enrollmentStatus;
    private LocalDateTime enrolledDate;
    private LocalDateTime approvedDate;
    private LocalDateTime declinedDate;
    private LocalDateTime agentApprovedDate;
    private LocalDateTime agentDeclinedDate;
    private String agentNotes;
    private String adminNotes;
    private String generatedPolicyNumber;

    // Policy template details
    private String vehicleDetails;
    private Double coverageAmount;
    private String coverageType;
    private Double premiumAmount;

    // Default constructor
    public PolicyEnrollmentResponse() {
    }

    // Constructor from PolicyEnrollment entity
    public PolicyEnrollmentResponse(PolicyEnrollment enrollment) {
        this.enrollmentId = enrollment.getEnrollmentId();
        this.policyTemplateId = enrollment.getPolicyTemplate().getPolicyId();
        this.policyTemplateNumber = enrollment.getPolicyTemplate().getPolicyNumber();
        this.customerName = enrollment.getCustomer().getUsername();
        this.customerEmail = enrollment.getCustomer().getEmail();
        this.agentName = enrollment.getAgent() != null ? enrollment.getAgent().getUsername() : null;
        this.enrollmentStatus = enrollment.getEnrollmentStatus().name();
        this.enrolledDate = enrollment.getEnrolledDate();
        this.approvedDate = enrollment.getApprovedDate();
        this.declinedDate = enrollment.getDeclinedDate();
        this.agentApprovedDate = enrollment.getAgentApprovedDate();
        this.agentDeclinedDate = enrollment.getAgentDeclinedDate();
        this.agentNotes = enrollment.getAgentNotes();
        this.adminNotes = enrollment.getAdminNotes();
        this.generatedPolicyNumber = enrollment.getGeneratedPolicyNumber();

        // Policy template details
        this.vehicleDetails = enrollment.getPolicyTemplate().getVehicleDetails();
        this.coverageAmount = enrollment.getPolicyTemplate().getCoverageAmount().doubleValue();
        this.coverageType = enrollment.getPolicyTemplate().getCoverageType();
        this.premiumAmount = enrollment.getPolicyTemplate().getPremiumAmount().doubleValue();
    }

    // Getters and Setters
    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Long getPolicyTemplateId() {
        return policyTemplateId;
    }

    public void setPolicyTemplateId(Long policyTemplateId) {
        this.policyTemplateId = policyTemplateId;
    }

    public String getPolicyTemplateNumber() {
        return policyTemplateNumber;
    }

    public void setPolicyTemplateNumber(String policyTemplateNumber) {
        this.policyTemplateNumber = policyTemplateNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public LocalDateTime getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(LocalDateTime enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    public LocalDateTime getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(LocalDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }

    public LocalDateTime getDeclinedDate() {
        return declinedDate;
    }

    public void setDeclinedDate(LocalDateTime declinedDate) {
        this.declinedDate = declinedDate;
    }

    public LocalDateTime getAgentApprovedDate() {
        return agentApprovedDate;
    }

    public void setAgentApprovedDate(LocalDateTime agentApprovedDate) {
        this.agentApprovedDate = agentApprovedDate;
    }

    public LocalDateTime getAgentDeclinedDate() {
        return agentDeclinedDate;
    }

    public void setAgentDeclinedDate(LocalDateTime agentDeclinedDate) {
        this.agentDeclinedDate = agentDeclinedDate;
    }

    public String getAgentNotes() {
        return agentNotes;
    }

    public void setAgentNotes(String agentNotes) {
        this.agentNotes = agentNotes;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public String getGeneratedPolicyNumber() {
        return generatedPolicyNumber;
    }

    public void setGeneratedPolicyNumber(String generatedPolicyNumber) {
        this.generatedPolicyNumber = generatedPolicyNumber;
    }

    public String getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(String vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public Double getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(Double coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public String getCoverageType() {
        return coverageType;
    }

    public void setCoverageType(String coverageType) {
        this.coverageType = coverageType;
    }

    public Double getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(Double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }
}
