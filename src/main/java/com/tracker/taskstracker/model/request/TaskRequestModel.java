package com.tracker.taskstracker.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TaskRequestModel {

    @NotBlank
    private String name;
    private String state;
    @NotBlank
    private String type;
    @NotNull
    private String description;
    @NotBlank
    private String projectName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
