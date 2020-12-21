package com.tracker.taskstracker.model.request;

import javax.validation.constraints.Pattern;

public class ForgottenPasswordRequestModel {

    @Pattern(regexp = "^[A-Za-z][A-Za-z.0-9]+@([A-Za-z]+(\\.)){1,}[A-Za-z0-9]+$")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
