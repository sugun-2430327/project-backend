package com.autoinsurance.insurance.dto;

import com.autoinsurance.insurance.model.User;

public class UserResponse {

    private Long userId;
    private String username;
    private String email;
    private String role;
    private Double incomePerAnnum;
    private String idProofFilePath;

    // Default constructor
    public UserResponse() {
    }

    // Constructor from User entity
    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.incomePerAnnum = user.getIncomePerAnnum();
        this.idProofFilePath = user.getIdProofFilePath();
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
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
