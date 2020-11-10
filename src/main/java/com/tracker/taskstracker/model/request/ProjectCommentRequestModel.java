package com.tracker.taskstracker.model.request;

import javax.validation.constraints.NotBlank;

public class ProjectCommentRequestModel extends CommentRequestModel {

    @NotBlank
    private String projectId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
