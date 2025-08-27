package com.autoinsurance.insurance.dto;

import jakarta.validation.constraints.Size;

public class EnrollmentRequest {

    @Size(max = 1000, message = "Vehicle details cannot exceed 1000 characters")
    private String vehicleDetails;

    // Default constructor
    public EnrollmentRequest() {
    }

    // Constructor
    public EnrollmentRequest(String vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    // Getters and Setters
    public String getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(String vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }
}
