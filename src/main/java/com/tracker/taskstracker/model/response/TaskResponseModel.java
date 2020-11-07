package com.tracker.taskstracker.model.response;

import java.time.LocalDate;

public class TaskResponseModel extends IdModel {

    private String name;
    private String state;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String description;
    private ProjectResponseModel project;
    private UserResponseModel assignee;

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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectResponseModel getProject() {
        return project;
    }

    public void setProject(ProjectResponseModel project) {
        this.project = project;
    }

    public UserResponseModel getAssignee() {
        return assignee;
    }

    public void setAssignee(UserResponseModel assignee) {
        this.assignee = assignee;
    }
}
