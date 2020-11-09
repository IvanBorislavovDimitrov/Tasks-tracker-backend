package com.tracker.taskstracker.model.response;

public class CommentResponseModel extends IdModel {

    private String description;
    private UserResponseModel author;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserResponseModel getAuthor() {
        return author;
    }

    public void setAuthor(UserResponseModel author) {
        this.author = author;
    }
}
