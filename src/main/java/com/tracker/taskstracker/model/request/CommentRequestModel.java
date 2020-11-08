package com.tracker.taskstracker.model.request;

import javax.validation.constraints.NotBlank;

public class CommentRequestModel {

    @NotBlank
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
