package com.autoinsurance.insurance.dto;

import com.autoinsurance.insurance.model.PolicyEnrollment;
import java.time.LocalDateTime;

public class PolicyEnrollmentResponse {

    private Long enrollmentId;
    private Long policyTemplateId;
    private String policyTemplateNumber;
    private String customerName;
    private String customerEmail;
    private String firstName;
    private String lastName;

    private String enrollmentStatus;
    private LocalDateTime enrolledDate;
    private LocalDateTime approvedDate;
    private LocalDateTime declinedDate;

    private String adminNotes;
    private String generatedPolicyNumber;

    // Customer-specific enrollment details
    private String vehicleDetails;

    // Policy template details
    private String vehicleType;
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
        this.firstName = enrollment.getCustomer().getFirstName();
        this.lastName = enrollment.getCustomer().getLastName();

        this.enrollmentStatus = enrollment.getEnrollmentStatus().name();
        this.enrolledDate = enrollment.getEnrolledDate();
        this.approvedDate = enrollment.getApprovedDate();
        this.declinedDate = enrollment.getDeclinedDate();

        this.adminNotes = enrollment.getAdminNotes();
        this.generatedPolicyNumber = enrollment.getGeneratedPolicyNumber();

        // Customer-specific enrollment details
        this.vehicleDetails = enrollment.getVehicleDetails();

        // Policy template details
        this.vehicleType = enrollment.getPolicyTemplate().getVehicleType();
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

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
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
