package com.tracker.taskstracker.model.response;

import java.util.List;

public class UsersUsernamesResponseModel {

    private List<UserResponseModel> users;

    public List<UserResponseModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponseModel> users) {
        this.users = users;
    }
}
