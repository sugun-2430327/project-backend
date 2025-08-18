package com.autoinsurance.insurance.dto;

public class SupportTicketRequest {
    
    private String issueDescription;
    private Long policyEnrollmentId; // Optional: if ticket is related to a specific policy
    private Long claimId; // Optional: if ticket is related to a specific claim
    
    // Constructors
    public SupportTicketRequest() {}
    
    public SupportTicketRequest(String issueDescription) {
        this.issueDescription = issueDescription;
    }
    
    public SupportTicketRequest(String issueDescription, Long policyEnrollmentId, Long claimId) {
        this.issueDescription = issueDescription;
        this.policyEnrollmentId = policyEnrollmentId;
        this.claimId = claimId;
    }
    
    // Getters and Setters
    public String getIssueDescription() {
        return issueDescription;
    }
    
    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }
    
    public Long getPolicyEnrollmentId() {
        return policyEnrollmentId;
    }
    
    public void setPolicyEnrollmentId(Long policyEnrollmentId) {
        this.policyEnrollmentId = policyEnrollmentId;
    }
    
    public Long getClaimId() {
        return claimId;
    }
    
    public void setClaimId(Long claimId) {
        this.claimId = claimId;
    }
}
