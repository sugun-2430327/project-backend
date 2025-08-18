package com.autoinsurance.insurance.dto;

public class EnrollmentEligibilityResponse {
    private boolean canEnroll;
    private String message;
    private String currentStatus;

    // Default constructor
    public EnrollmentEligibilityResponse() {
    }

    // Constructor
    public EnrollmentEligibilityResponse(boolean canEnroll, String message, String currentStatus) {
        this.canEnroll = canEnroll;
        this.message = message;
        this.currentStatus = currentStatus;
    }

    // Getters and Setters
    public boolean isCanEnroll() {
        return canEnroll;
    }

    public void setCanEnroll(boolean canEnroll) {
        this.canEnroll = canEnroll;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
}
