package com.tracker.taskstracker.model.request;

import javax.validation.constraints.NotBlank;

public class ReleaseRequestModel {

    @NotBlank
    private String version;
    @NotBlank
    private String projectId;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
