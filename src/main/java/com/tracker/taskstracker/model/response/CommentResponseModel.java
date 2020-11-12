package com.tracker.taskstracker.model.response;

import java.util.Date;

public class CommentResponseModel extends IdModel {

    private String description;
    private UserResponseModel author;
    private Date createdAt;
    private Date updatedAt;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
