package com.tracker.taskstracker.model.response;

public class AuthenticationResponseModel extends IdModel {

    private final String token;

    public AuthenticationResponseModel(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
