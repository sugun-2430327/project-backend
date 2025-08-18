package com.autoinsurance.insurance.dto;

import com.autoinsurance.insurance.model.ClaimStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ClaimUpdateRequest {

    @NotNull(message = "Claim status is required")
    private ClaimStatus claimStatus;

    @Size(max = 1000, message = "Agent notes cannot exceed 1000 characters")
    private String agentNotes;

    // Default constructor
    public ClaimUpdateRequest() {
    }

    // Constructor
    public ClaimUpdateRequest(ClaimStatus claimStatus, String agentNotes) {
        this.claimStatus = claimStatus;
        this.agentNotes = agentNotes;
    }

    // Getters and Setters
    public ClaimStatus getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(ClaimStatus claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getAgentNotes() {
        return agentNotes;
    }

    public void setAgentNotes(String agentNotes) {
        this.agentNotes = agentNotes;
    }
}
