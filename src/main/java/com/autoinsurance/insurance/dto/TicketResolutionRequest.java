package com.autoinsurance.insurance.dto;

public class TicketResolutionRequest {
    
    private String resolution;
    
    // Constructors
    public TicketResolutionRequest() {}
    
    public TicketResolutionRequest(String resolution) {
        this.resolution = resolution;
    }
    
    // Getters and Setters
    public String getResolution() {
        return resolution;
    }
    
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
