package com.autoinsurance.insurance.dto;

import com.autoinsurance.insurance.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Role role;

    private Double incomePerAnnum;

    // File path will be set after file upload processing
    private String idProofFilePath;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Double getIncomePerAnnum() {
        return incomePerAnnum;
    }

    public void setIncomePerAnnum(Double incomePerAnnum) {
        this.incomePerAnnum = incomePerAnnum;
    }

    public String getIdProofFilePath() {
        return idProofFilePath;
    }

    public void setIdProofFilePath(String idProofFilePath) {
        this.idProofFilePath = idProofFilePath;
    }
}
