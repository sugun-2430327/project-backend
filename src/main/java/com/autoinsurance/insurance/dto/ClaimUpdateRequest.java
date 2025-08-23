package com.autoinsurance.insurance.dto;

import com.autoinsurance.insurance.model.ClaimStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ClaimUpdateRequest {

    @NotNull(message = "Claim status is required")
    private ClaimStatus claimStatus;

    @Size(max = 1000, message = "Admin notes cannot exceed 1000 characters")
    private String adminNotes;

    // Default constructor
    public ClaimUpdateRequest() {
    }

    // Constructor
    public ClaimUpdateRequest(ClaimStatus claimStatus, String adminNotes) {
        this.claimStatus = claimStatus;
        this.adminNotes = adminNotes;
    }

    // Getters and Setters
    public ClaimStatus getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(ClaimStatus claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }
}
