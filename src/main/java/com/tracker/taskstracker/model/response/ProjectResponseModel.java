package com.tracker.taskstracker.model.response;

import java.util.ArrayList;
import java.util.List;

public class ProjectResponseModel extends IdModel {

    private String name;
    private String description;
    private List<UserResponseModel> users = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<UserResponseModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponseModel> users) {
        this.users = users;
    }
}
