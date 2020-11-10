package com.tracker.taskstracker.model.response;

public class TaskCommentResponseModel extends CommentResponseModel {

    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
