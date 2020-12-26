package com.tracker.taskstracker.model.request;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UserRolesRequestModel {

    @NotNull
    private String username;
    @NotNull
    private List<String> roles;

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
