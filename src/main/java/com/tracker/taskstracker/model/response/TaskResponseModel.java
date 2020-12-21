package com.tracker.taskstracker.model.response;

import java.util.Date;

public class TaskResponseModel extends IdModel {

    private String name;
    private String state;
    private String type;
    private Date createdAt;
    private Date updatedAt;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
