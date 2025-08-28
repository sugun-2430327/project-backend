package com.autoinsurance.insurance.dto;

import com.autoinsurance.insurance.model.SupportTicket;
import com.autoinsurance.insurance.model.TicketStatus;
import java.time.LocalDate;

public class SupportTicketResponse {
    
    private Long ticketId;
    private Long userId;
    private String customerUsername;
    private String customerEmail;
    private String firstName;
    private String lastName;
    private String issueDescription;
    private TicketStatus ticketStatus;
    private LocalDate createdDate;
    private LocalDate resolvedDate;
    private String resolution;
    private String resolvedByUsername;
    
    // Related policy/claim information
    private Long policyEnrollmentId;
    private String policyNumber;
    private Long claimId;
    
    // Constructors
    public SupportTicketResponse() {}
    
    public SupportTicketResponse(SupportTicket ticket) {
        this.ticketId = ticket.getTicketId();
        this.userId = ticket.getUser().getUserId();
        this.customerUsername = ticket.getUser().getUsername();
        this.customerEmail = ticket.getUser().getEmail();
        this.firstName = ticket.getUser().getFirstName();
        this.lastName = ticket.getUser().getLastName();
        this.issueDescription = ticket.getIssueDescription();
        this.ticketStatus = ticket.getTicketStatus();
        this.createdDate = ticket.getCreatedDate();
        this.resolvedDate = ticket.getResolvedDate();
        this.resolution = ticket.getResolution();
        
        if (ticket.getResolvedBy() != null) {
            this.resolvedByUsername = ticket.getResolvedBy().getUsername();
        }
        
        if (ticket.getPolicyEnrollment() != null) {
            this.policyEnrollmentId = ticket.getPolicyEnrollment().getEnrollmentId();
            this.policyNumber = ticket.getPolicyEnrollment().getPolicyTemplate().getPolicyNumber();
        }
        
        if (ticket.getClaim() != null) {
            this.claimId = ticket.getClaim().getClaimId();
        }
    }
    
    // Getters and Setters
    public Long getTicketId() {
        return ticketId;
    }
    
    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
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
    
    public String getIssueDescription() {
        return issueDescription;
    }
    
    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }
    
    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }
    
    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }
    
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    
    public LocalDate getResolvedDate() {
        return resolvedDate;
    }
    
    public void setResolvedDate(LocalDate resolvedDate) {
        this.resolvedDate = resolvedDate;
    }
    
    public String getResolution() {
        return resolution;
    }
    
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
    
    public String getResolvedByUsername() {
        return resolvedByUsername;
    }
    
    public void setResolvedByUsername(String resolvedByUsername) {
        this.resolvedByUsername = resolvedByUsername;
    }
    
    public Long getPolicyEnrollmentId() {
        return policyEnrollmentId;
    }
    
    public void setPolicyEnrollmentId(Long policyEnrollmentId) {
        this.policyEnrollmentId = policyEnrollmentId;
    }
    
    public String getPolicyNumber() {
        return policyNumber;
    }
    
    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }
    
    public Long getClaimId() {
        return claimId;
    }
    
    public void setClaimId(Long claimId) {
        this.claimId = claimId;
    }
}
