package com.tracker.taskstracker.model.request;

import javax.validation.constraints.NotBlank;

public class ReleaseRequestModel {

    @NotBlank
    private String version;
    @NotBlank
    private String projectName;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
