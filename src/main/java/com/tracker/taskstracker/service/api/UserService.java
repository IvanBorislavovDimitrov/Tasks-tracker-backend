package com.tracker.taskstracker.service.api;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tracker.taskstracker.model.request.UserRequestModel;
import com.tracker.taskstracker.model.response.UserResponseModel;

public interface UserService extends UserDetailsService, GenericService<UserRequestModel, UserResponseModel, String> {

    UserResponseModel register(UserRequestModel userRequestModel);

    UserResponseModel findByUsername(String username);
}
