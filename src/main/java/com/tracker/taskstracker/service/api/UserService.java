package com.tracker.taskstracker.service.api;

import com.tracker.taskstracker.model.request.UserRequestModel;
import com.tracker.taskstracker.model.response.UserResponseModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends UserDetailsService, GenericService<UserRequestModel, UserResponseModel, String> {

    UserResponseModel register(UserRequestModel userRequestModel);

    UserResponseModel findByUsername(String username);

    UserResponseModel activateAccount(String userActivationCode);

    UserResponseModel updateProfilePicture(String username, MultipartFile profilePicture);

    void saveUserLoginRecord(String username);

    UserResponseModel findExtendedByUsername(String username);
}
