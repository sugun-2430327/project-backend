package com.autoinsurance.insurance.dto;

import jakarta.validation.constraints.NotNull;

public class ApprovalRequest {

    @NotNull(message = "Agent ID is required for approval")
    private Long agentId;

    private String notes; // Optional notes from admin

    // Default constructor
    public ApprovalRequest() {
    }

    // Constructor
    public ApprovalRequest(Long agentId, String notes) {
        this.agentId = agentId;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
