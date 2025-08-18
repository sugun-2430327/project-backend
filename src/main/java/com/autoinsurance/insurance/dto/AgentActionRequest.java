package com.autoinsurance.insurance.dto;

public class AgentActionRequest {
    private String action;  // "approve" or "decline"
    private String notes;

    // Default constructor
    public AgentActionRequest() {
    }

    // Constructor
    public AgentActionRequest(String action, String notes) {
        this.action = action;
        this.notes = notes;
    }

    // Getters and Setters
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
