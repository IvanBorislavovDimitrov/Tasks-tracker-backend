package com.tracker.taskstracker.model.request;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserRequestModel {

    @Column(unique = true)
    @Pattern(regexp = "^[A-Za-z][A-Za-z.0-9]+@([A-Za-z]+(\\.)){1,}[A-Za-z0-9]+$")
    private String email;
    @NotBlank
    @Column(unique = true)
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
