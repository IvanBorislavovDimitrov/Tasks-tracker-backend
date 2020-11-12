package com.tracker.taskstracker.model.request;

import javax.validation.constraints.NotBlank;

public class TaskStateRequestModel {

    @NotBlank
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
