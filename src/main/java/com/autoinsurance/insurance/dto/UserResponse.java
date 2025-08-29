package com.autoinsurance.insurance.dto;

import com.autoinsurance.insurance.model.User;

public class UserResponse {

    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String role;
    private Integer age;
    private String idProofFilePath;

    // Default constructor
    public UserResponse() {
    }

    // Constructor from User entity
    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.age = user.getAge();
        this.idProofFilePath = user.getIdProofFilePath();
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIdProofFilePath() {
        return idProofFilePath;
    }

    public void setIdProofFilePath(String idProofFilePath) {
        this.idProofFilePath = idProofFilePath;
    }
}
