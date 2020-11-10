package com.tracker.taskstracker.model.response;

public class ProjectCommentResponseModel extends CommentResponseModel {

    private String projectId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
