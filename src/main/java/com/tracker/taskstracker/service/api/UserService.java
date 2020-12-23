package com.tracker.taskstracker.service.api;

import com.tracker.taskstracker.model.request.ChangeForgottenPasswordRequestModel;
import com.tracker.taskstracker.model.request.UpdateUserPasswordRequestModel;
import com.tracker.taskstracker.model.request.UserRequestModel;
import com.tracker.taskstracker.model.response.UserResponseModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends UserDetailsService, GenericService<UserRequestModel, UserResponseModel, String> {

    UserResponseModel register(UserRequestModel userRequestModel);

    UserResponseModel findByUsername(String username);

    UserResponseModel activateAccount(String userActivationCode);

    UserResponseModel updateProfilePicture(String username, MultipartFile profilePicture);

    void saveUserLoginRecord(String username);

    UserResponseModel findExtendedByUsername(String username);

    UserResponseModel updateUserPassword(String username, UpdateUserPasswordRequestModel updateUserPasswordRequestModel);

    UserResponseModel generateForgottenPasswordEmail(String email);

    UserResponseModel findByForgottenPasswordToken(String forgottenPasswordToken);

    UserResponseModel changeForgottenPassword(ChangeForgottenPasswordRequestModel changeForgottenPasswordRequestModel);

    List<UserResponseModel> findUsersByProjectId(String projectId);

    UserResponseModel findExtendedById(String userId);

}
