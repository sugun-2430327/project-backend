package com.autoinsurance.insurance.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "policy_enrollments")
public class PolicyEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    // Foreign key to Policy Template
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_template_id", nullable = false)
    private Policy policyTemplate;

    // Foreign key to Customer (enrolling user)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus enrollmentStatus;

    @Column(name = "enrolled_date", nullable = false)
    private LocalDateTime enrolledDate;

    @Column(name = "approved_date")
    private LocalDateTime approvedDate;

    @Column(name = "declined_date")
    private LocalDateTime declinedDate;

    @Column(name = "admin_notes")
    private String adminNotes;

    @Column(name = "generated_policy_number")
    private String generatedPolicyNumber; // Policy number generated for this enrollment

    // Default constructor
    public PolicyEnrollment() {
    }

    // Constructor
    public PolicyEnrollment(Policy policyTemplate, User customer, String generatedPolicyNumber) {
        this.policyTemplate = policyTemplate;
        this.customer = customer;
        this.enrollmentStatus = EnrollmentStatus.PENDING;
        this.enrolledDate = LocalDateTime.now();
        this.generatedPolicyNumber = generatedPolicyNumber;
    }

    // Getters and Setters
    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Policy getPolicyTemplate() {
        return policyTemplate;
    }

    public void setPolicyTemplate(Policy policyTemplate) {
        this.policyTemplate = policyTemplate;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public EnrollmentStatus getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(EnrollmentStatus enrollmentStatus) {
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

    // Enum for enrollment status
    public enum EnrollmentStatus {
        PENDING,            // Enrollment submitted, awaiting admin review
        APPROVED,           // Admin approved, policy is active
        DECLINED,           // Admin declined, user can re-enroll after some time
        WITHDRAWN           // User cancelled their own enrollment
    }
}
