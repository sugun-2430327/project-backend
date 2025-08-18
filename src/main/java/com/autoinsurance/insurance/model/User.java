package com.autoinsurance.insurance.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "income_per_annum")
    private Double incomePerAnnum;

    @Column(name = "id_proof_file_path")
    private String idProofFilePath;

    // Default constructor
    public User() {
    }

    // Constructor with fields
    public User(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Constructor with all fields including new ones
    public User(String username, String password, String email, Role role, Double incomePerAnnum, String idProofFilePath) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.incomePerAnnum = incomePerAnnum;
        this.idProofFilePath = idProofFilePath;
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

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return both the raw role name and the ROLE_ prefixed version for flexibility
        return List.of(
            new SimpleGrantedAuthority(role.name()),
            new SimpleGrantedAuthority("ROLE_" + role.name())
        );
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
