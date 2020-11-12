package com.tracker.taskstracker.model.response;

import java.util.List;

public class TaskResponseModelExtended extends TaskResponseModel {

    private List<TaskCommentResponseModel> comments;

    public List<TaskCommentResponseModel> getComments() {
        return comments;
    }

    public void setComments(List<TaskCommentResponseModel> comments) {
        this.comments = comments;
    }
}
