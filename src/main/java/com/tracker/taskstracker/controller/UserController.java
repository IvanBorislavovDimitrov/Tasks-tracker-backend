package com.tracker.taskstracker.controller;

import com.tracker.taskstracker.model.request.UpdateUserPasswordRequestModel;
import com.tracker.taskstracker.model.request.UserRequestModel;
import com.tracker.taskstracker.model.response.UserResponseModel;
import com.tracker.taskstracker.service.api.UserService;
import com.tracker.taskstracker.util.LoggedUserGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;
    private final LoggedUserGetter loggedUserGetter;

    @Autowired
    public UserController(UserService userService, LoggedUserGetter loggedUserGetter) {
        this.userService = userService;
        this.loggedUserGetter = loggedUserGetter;
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

    @GetMapping("/logged-user")
    public ResponseEntity<UserResponseModel> getLoggedUser() {
        String loggedUserUsername = loggedUserGetter.getUsername();
        return ResponseEntity.ok(userService.findExtendedByUsername(loggedUserUsername));
    }

    @PatchMapping("/activate/{activationCode}")
    public ResponseEntity<UserResponseModel> activateAccount(@PathVariable String activationCode) {
        UserResponseModel userResponseModel = userService.activateAccount(activationCode);
        return ResponseEntity.ok(userResponseModel);
    }

    @PatchMapping(value = "/update/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseModel> updateProfilePicture(MultipartFile profilePicture) {
        String loggedUserUsername = loggedUserGetter.getUsername();
        UserResponseModel userResponseModel = userService.updateProfilePicture(loggedUserUsername, profilePicture);
        return ResponseEntity.ok(userResponseModel);
    }

    @PatchMapping(value = "/update/password")
    public ResponseEntity<UserResponseModel> updatePassword(@RequestBody @Valid UpdateUserPasswordRequestModel
                                                                        updateUserPasswordRequestModel) {
        String loggedUserUsername = loggedUserGetter.getUsername();
        return ResponseEntity.ok(userService.updateUserPassword(loggedUserUsername, updateUserPasswordRequestModel));
    }

}
