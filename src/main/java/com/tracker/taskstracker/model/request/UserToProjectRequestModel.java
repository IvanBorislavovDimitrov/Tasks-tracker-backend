package com.tracker.taskstracker.model.request;

import javax.validation.constraints.NotBlank;

public class UserToProjectRequestModel {

    @NotBlank
    private String username;
    @NotBlank
    private String projectName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
