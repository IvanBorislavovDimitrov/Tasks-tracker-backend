package com.tracker.taskstracker.service.impl;

import com.tracker.taskstracker.constants.Constants;
import com.tracker.taskstracker.domain.Project;
import com.tracker.taskstracker.domain.Role;
import com.tracker.taskstracker.domain.User;
import com.tracker.taskstracker.exception.TRException;
import com.tracker.taskstracker.model.request.ChangeForgottenPasswordRequestModel;
import com.tracker.taskstracker.model.request.UpdateUserPasswordRequestModel;
import com.tracker.taskstracker.model.request.UserRequestModel;
import com.tracker.taskstracker.model.request.UserRolesRequestModel;
import com.tracker.taskstracker.model.response.UserResponseModel;
import com.tracker.taskstracker.model.response.UserResponseModelExtended;
import com.tracker.taskstracker.model.response.UsersUsernamesResponseModel;
import com.tracker.taskstracker.repository.ProjectRepository;
import com.tracker.taskstracker.repository.RoleRepository;
import com.tracker.taskstracker.repository.UserRepository;
import com.tracker.taskstracker.service.api.UserService;
import com.tracker.taskstracker.storage.FileService;
import com.tracker.taskstracker.storage.FileStorageGetter;
import com.tracker.taskstracker.util.FilesUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl extends GenericServiceImpl<User, UserRequestModel, UserResponseModel, String> implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JavaMailSender javaMailSender;
    private final ExecutorService executorService;
    private final FileService fileService;
    private final ProjectRepository projectRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository, JavaMailSender javaMailSender, ExecutorService executorService,
                           FileStorageGetter fileStorageGetter, ProjectRepository projectRepository) {
        super(userRepository, modelMapper);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.javaMailSender = javaMailSender;
        this.executorService = executorService;
        this.fileService = fileStorageGetter.getFileService();
        this.projectRepository = projectRepository;
    }

    @Override
    public UserResponseModel register(UserRequestModel userRequestModel) {
        validatePasswords(userRequestModel.getPassword(), userRequestModel.getConfirmPassword());
        String encryptedPassword = passwordEncoder.encode(userRequestModel.getPassword());
        userRequestModel.setPassword(encryptedPassword);
        User user = modelMapper.map(userRequestModel, User.class);
        addDefaultRoles(user);
        sendActivationEmail(user);
        return modelMapper.map(userRepository.save(user), UserResponseModel.class);
    }

    private void addDefaultRoles(User user) {
        Role userRole = roleRepository.findByType(Role.Type.USER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        user.addRole(userRole);
        userRole.addUser(user);
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByType(Role.Type.ADMIN)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
            user.addRole(adminRole);
            adminRole.addUser(user);
        }
    }

    private void sendActivationEmail(User user) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("automaticmailsendercommunity@gmail.com");
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Please activate your account!");
        UUID activationCode = UUID.randomUUID();
        user.setAccountVerificationCode(activationCode.toString());
        simpleMailMessage.setText("Click here: " + "http://localhost:3000/users/activate/" + activationCode);
        executorService.execute(() -> javaMailSender.send(simpleMailMessage));
    }

    @Override
    public UserResponseModel findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return modelMapper.map(user, UserResponseModel.class);
    }

    private void validatePasswords(String password, String confirmPassword) {
        if (!Objects.equals(password, confirmPassword)) {
            throw new TRException("Passwords do not match");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public UserResponseModel activateAccount(String userActivationCode) {
        User user = userRepository.findByAccountVerificationCode(userActivationCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to perform this request"));
        user.setEnabled(true);
        return modelMapper.map(userRepository.save(user), UserResponseModel.class);
    }

    @Override
    public UserResponseModel updateProfilePicture(String username, MultipartFile profilePicture) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setProfilePictureName(user.getUsername() + Constants.USERNAME_PROFILE_PICTURE_SUFFIX +
                FilesUtil.getFileExtension(profilePicture));
        userRepository.save(user);
        fileService.save(user.getProfilePictureName(), profilePicture);
        return modelMapper.map(user, UserResponseModel.class);
    }

    @Override
    public void saveUserLoginRecord(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        User.LoginRecord loginRecord = new User.LoginRecord();
        loginRecord.setCreatedAt(new Date());
        user.addLoginRecord(loginRecord);
        if (user.getLoginRecords().size() > 10) {
            user.removeOldestLoginRecord();
        }
        userRepository.save(user);
    }

    @Override
    public UserResponseModel findExtendedByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return modelMapper.map(user, UserResponseModelExtended.class);
    }

    @Override
    public UserResponseModel updateUserPassword(String username, UpdateUserPasswordRequestModel updateUserPasswordRequestModel) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!passwordEncoder.matches(updateUserPasswordRequestModel.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Current password is invalid!");
        }
        validatePasswords(updateUserPasswordRequestModel.getNewPassword(), updateUserPasswordRequestModel.getConfirmNewPassword());
        user.setPassword(passwordEncoder.encode(updateUserPasswordRequestModel.getNewPassword()));
        return modelMapper.map(user, UserResponseModel.class);
    }

    @Override
    public UserResponseModel generateForgottenPasswordEmail(String email) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("automaticmailsendercommunity@gmail.com");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Reset your password!");
        UUID forgottenPasswordToken = UUID.randomUUID();
        user.setForgottenPasswordToken(forgottenPasswordToken.toString());
        simpleMailMessage.setText("Click here: " + "http://localhost:3000/users/reset-password/" + forgottenPasswordToken);
        executorService.execute(() -> javaMailSender.send(simpleMailMessage));
        return modelMapper.map(userRepository.save(user), UserResponseModel.class);
    }

    @Override
    public UserResponseModel findByForgottenPasswordToken(String forgottenPasswordToken) {
        User user = userRepository.findByForgottenPasswordToken(forgottenPasswordToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User by token found"));
        return modelMapper.map(user, UserResponseModel.class);
    }

    @Override
    public UserResponseModel changeForgottenPassword(ChangeForgottenPasswordRequestModel changeForgottenPasswordRequestModel) {
        User user = userRepository.findByUsername(changeForgottenPasswordRequestModel.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!Objects.equals(user.getForgottenPasswordToken(), changeForgottenPasswordRequestModel.getToken())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tokens do no match");
        }
        validatePasswords(changeForgottenPasswordRequestModel.getNewPassword(),
                changeForgottenPasswordRequestModel.getConfirmNewPassword());
        user.setPassword(passwordEncoder.encode(changeForgottenPasswordRequestModel.getNewPassword()));
        user.setForgottenPasswordToken(null);
        return modelMapper.map(userRepository.save(user), UserResponseModel.class);
    }

    @Override
    public List<UserResponseModel> findUsersByProjectId(String projectId) {
        return projectRepository.findById(projectId)
                .map(Project::getUsers)
                .orElseThrow(() -> new TRException("Project not found"))
                .stream()
                .distinct()
                .map(user -> modelMapper.map(user, UserResponseModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseModel findExtendedById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return modelMapper.map(user, UserResponseModelExtended.class);
    }

    @Override
    public UserResponseModel updateUserRoles(UserRolesRequestModel userRolesRequestModel) {
        User user = userRepository.findByUsername(userRolesRequestModel.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<Role> newRoles = findRolesByTypes(userRolesRequestModel.getRoles());
        List<Role> rolesThatAreNotAssignedAnymore = getRolesThatAreNotAssignedAnymore(user.getRoles(), newRoles);
        rolesThatAreNotAssignedAnymore.forEach(role -> role.getUsers().remove(user));
        newRoles.forEach(role -> role.addUserIfNotExists(user));
        user.setRoles(newRoles);
        return modelMapper.map(userRepository.save(user), UserResponseModel.class);
    }

    private List<Role> getRolesThatAreNotAssignedAnymore(List<Role> currentRoles, List<Role> newRoles) {
        return currentRoles.stream()
                .filter(currentRole -> !newRoles.contains(currentRole))
                .collect(Collectors.toList());

    }

    private List<Role> findRolesByTypes(List<String> userRoles) {
        return userRoles.stream()
                .map(userRole -> roleRepository.findByType(Role.Type.valueOf(userRole)))
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public UsersUsernamesResponseModel findAllStartingWith(String username) {
        List<UserResponseModel> userResponseModels = userRepository.findAllByUsernameStartingWith(username)
                .stream()
                .map(user -> modelMapper.map(user, UserResponseModel.class))
                .collect(Collectors.toList());
        UsersUsernamesResponseModel usersUsernamesResponseModel = new UsersUsernamesResponseModel();
        usersUsernamesResponseModel.setUsers(userResponseModels);
        return usersUsernamesResponseModel;
    }

    @Override
    protected Class<UserResponseModel> getOutputModelClass() {
        return UserResponseModel.class;
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }
}
