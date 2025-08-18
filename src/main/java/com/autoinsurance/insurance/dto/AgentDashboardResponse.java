package com.autoinsurance.insurance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AgentDashboardResponse {
    
    private Long agentId;
    private String agentName;
    private String agentEmail;
    private List<AssignedPolicyEnrollment> assignedPolicies;
    
    // Default constructor
    public AgentDashboardResponse() {}
    
    // Constructor
    public AgentDashboardResponse(Long agentId, String agentName, String agentEmail, List<AssignedPolicyEnrollment> assignedPolicies) {
        this.agentId = agentId;
        this.agentName = agentName;
        this.agentEmail = agentEmail;
        this.assignedPolicies = assignedPolicies;
    }
    
    // Nested class for policy enrollment details
    public static class AssignedPolicyEnrollment {
        private Long enrollmentId;
        private String generatedPolicyNumber;
        private String enrollmentStatus;
        private LocalDateTime enrolledDate;
        private LocalDateTime approvedDate;
        private String adminNotes;
        
        // Customer details
        private Long customerId;
        private String customerName;
        private String customerEmail;
        private String customerPhone;
        
        // Policy template details
        private Long policyTemplateId;
        private String policyNumber;
        private String vehicleDetails;
        private BigDecimal coverageAmount;
        private String coverageType;
        private BigDecimal premiumAmount;
        private LocalDate startDate;
        private LocalDate endDate;
        private String policyStatus;
        
        // Default constructor
        public AssignedPolicyEnrollment() {}
        
        // Getters and Setters
        public Long getEnrollmentId() {
            return enrollmentId;
        }
        
        public void setEnrollmentId(Long enrollmentId) {
            this.enrollmentId = enrollmentId;
        }
        
        public String getGeneratedPolicyNumber() {
            return generatedPolicyNumber;
        }
        
        public void setGeneratedPolicyNumber(String generatedPolicyNumber) {
            this.generatedPolicyNumber = generatedPolicyNumber;
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
        
        public String getAdminNotes() {
            return adminNotes;
        }
        
        public void setAdminNotes(String adminNotes) {
            this.adminNotes = adminNotes;
        }
        
        public Long getCustomerId() {
            return customerId;
        }
        
        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
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
        
        public String getCustomerPhone() {
            return customerPhone;
        }
        
        public void setCustomerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
        }
        
        public Long getPolicyTemplateId() {
            return policyTemplateId;
        }
        
        public void setPolicyTemplateId(Long policyTemplateId) {
            this.policyTemplateId = policyTemplateId;
        }
        
        public String getPolicyNumber() {
            return policyNumber;
        }
        
        public void setPolicyNumber(String policyNumber) {
            this.policyNumber = policyNumber;
        }
        
        public String getVehicleDetails() {
            return vehicleDetails;
        }
        
        public void setVehicleDetails(String vehicleDetails) {
            this.vehicleDetails = vehicleDetails;
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
    }
    
    // Getters and Setters for main class
    public Long getAgentId() {
        return agentId;
    }
    
    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }
    
    public String getAgentName() {
        return agentName;
    }
    
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
    
    public String getAgentEmail() {
        return agentEmail;
    }
    
    public void setAgentEmail(String agentEmail) {
        this.agentEmail = agentEmail;
    }
    
    public List<AssignedPolicyEnrollment> getAssignedPolicies() {
        return assignedPolicies;
    }
    
    public void setAssignedPolicies(List<AssignedPolicyEnrollment> assignedPolicies) {
        this.assignedPolicies = assignedPolicies;
    }
}
