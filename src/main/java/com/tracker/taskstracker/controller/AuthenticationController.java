package com.tracker.taskstracker.controller;

import javax.validation.Valid;

import com.tracker.taskstracker.model.response.UserResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tracker.taskstracker.jwt.JwtUtil;
import com.tracker.taskstracker.model.request.AuthenticationRequest;
import com.tracker.taskstracker.model.response.AuthenticationResponse;
import com.tracker.taskstracker.service.api.UserService;

@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                                                                                            authenticationRequest.getPassword());
        authenticate(token);
        UserResponseModel userResponseModel = userService.getByUsername(authenticationRequest.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(JwtUtil.generateToken(userResponseModel)));
    }

    private void authenticate(UsernamePasswordAuthenticationToken token) {
        try {
            authenticationManager.authenticate(token);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect username or password!");
        }
    }

    @GetMapping(value = "/validate")
    public ResponseEntity<Void> validateLogin() {
        return ResponseEntity.ok()
                             .build();
    }
}
