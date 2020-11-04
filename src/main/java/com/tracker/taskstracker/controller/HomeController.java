package com.tracker.taskstracker.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.taskstracker.model.response.AuthenticationResponseModel;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponseModel> hello() {
        return ResponseEntity.ok(new AuthenticationResponseModel("123"));
    }
}
