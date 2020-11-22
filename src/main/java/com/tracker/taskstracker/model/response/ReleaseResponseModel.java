package com.tracker.taskstracker.model.response;

import java.util.List;

public class ReleaseResponseModel extends IdModel {

    private String version;
    private List<TaskResponseModel> tasks;
    private ProjectResponseModel project;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<TaskResponseModel> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskResponseModel> tasks) {
        this.tasks = tasks;
    }

    public ProjectResponseModel getProject() {
        return project;
    }

    public void setProject(ProjectResponseModel project) {
        this.project = project;
    }
}
