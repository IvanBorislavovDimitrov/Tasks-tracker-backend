package com.tracker.taskstracker.service.impl;

import com.tracker.taskstracker.constants.Constants;
import com.tracker.taskstracker.domain.Role;
import com.tracker.taskstracker.domain.User;
import com.tracker.taskstracker.exception.TRException;
import com.tracker.taskstracker.model.request.UserRequestModel;
import com.tracker.taskstracker.model.response.UserResponseModel;
import com.tracker.taskstracker.repository.RoleRepository;
import com.tracker.taskstracker.repository.UserRepository;
import com.tracker.taskstracker.service.api.FileService;
import com.tracker.taskstracker.service.api.UserService;
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

import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Service
@Transactional
public class UserServiceImpl extends GenericServiceImpl<User, UserRequestModel, UserResponseModel, String> implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JavaMailSender javaMailSender;
    private final ExecutorService executorService;
    private final FileService fileService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository, JavaMailSender javaMailSender, ExecutorService executorService,
                           FileService fileService) {
        super(userRepository, modelMapper);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.javaMailSender = javaMailSender;
        this.executorService = executorService;
        this.fileService = fileService;
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
    protected Class<UserResponseModel> getOutputModelClass() {
        return UserResponseModel.class;
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }
}
