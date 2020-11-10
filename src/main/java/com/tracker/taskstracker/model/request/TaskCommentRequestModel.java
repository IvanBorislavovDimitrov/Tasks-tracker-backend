package com.tracker.taskstracker.model.request;

import javax.validation.constraints.NotBlank;

public class TaskCommentRequestModel {

    @NotBlank
    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
