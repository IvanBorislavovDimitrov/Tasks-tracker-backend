package com.tracker.taskstracker.model.response;

import java.util.List;

public class ProjectResponseModelExtended extends ProjectResponseModel {

    private List<UserResponseModel> users;
    private List<CommentResponseModel> comments;

    public List<UserResponseModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponseModel> users) {
        this.users = users;
    }

    public List<CommentResponseModel> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponseModel> comments) {
        this.comments = comments;
    }
}
