package com.autoinsurance.insurance.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "support_tickets")
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    // Foreign key to User (customer who created the ticket)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String issueDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus ticketStatus;

    @Column(nullable = false)
    private LocalDate createdDate;

    @Column
    private LocalDate resolvedDate;

    // Optional: Link to policy enrollment if ticket is policy-related
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_enrollment_id")
    private PolicyEnrollment policyEnrollment;

    // Optional: Link to claim if ticket is claim-related
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id")
    private Claim claim;

    @Column(columnDefinition = "TEXT")
    private String resolution;

    // Agent/Admin who resolved the ticket
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by")
    private User resolvedBy;

    // Constructors
    public SupportTicket() {}

    public SupportTicket(User user, String issueDescription, TicketStatus ticketStatus, LocalDate createdDate) {
        this.user = user;
        this.issueDescription = issueDescription;
        this.ticketStatus = ticketStatus;
        this.createdDate = createdDate;
    }

    public SupportTicket(User user, String issueDescription, TicketStatus ticketStatus, LocalDate createdDate, 
                        PolicyEnrollment policyEnrollment, Claim claim) {
        this.user = user;
        this.issueDescription = issueDescription;
        this.ticketStatus = ticketStatus;
        this.createdDate = createdDate;
        this.policyEnrollment = policyEnrollment;
        this.claim = claim;
    }

    // Getters and Setters
    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public PolicyEnrollment getPolicyEnrollment() {
        return policyEnrollment;
    }

    public void setPolicyEnrollment(PolicyEnrollment policyEnrollment) {
        this.policyEnrollment = policyEnrollment;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public User getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(User resolvedBy) {
        this.resolvedBy = resolvedBy;
    }
}
