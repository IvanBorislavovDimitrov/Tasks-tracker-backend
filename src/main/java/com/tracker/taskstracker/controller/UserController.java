package com.tracker.taskstracker.controller;

import com.tracker.taskstracker.model.request.UserRequestModel;
import com.tracker.taskstracker.model.response.UserResponseModel;
import com.tracker.taskstracker.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseModel> register(@Valid @RequestBody UserRequestModel userRequestModel) {
        UserResponseModel userResponseModel = userService.register(userRequestModel);
        return ResponseEntity.ok(userResponseModel);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseModel> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @PatchMapping("/activate/{activationCode}")
    public ResponseEntity<UserResponseModel> activateAccount(@PathVariable String activationCode) {
        UserResponseModel userResponseModel = userService.activateAccount(activationCode);
        return ResponseEntity.ok(userResponseModel);
    }

}
