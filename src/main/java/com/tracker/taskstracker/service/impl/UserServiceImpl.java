package com.tracker.taskstracker.service.impl;

import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tracker.taskstracker.domain.Role;
import com.tracker.taskstracker.domain.User;
import com.tracker.taskstracker.domain.enums.RoleType;
import com.tracker.taskstracker.exception.TRException;
import com.tracker.taskstracker.model.request.UserRequestModel;
import com.tracker.taskstracker.model.response.UserResponseModel;
import com.tracker.taskstracker.repository.RoleRepository;
import com.tracker.taskstracker.repository.UserRepository;
import com.tracker.taskstracker.service.api.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserResponseModel register(UserRequestModel userRequestModel) {
        validatePasswords(userRequestModel.getPassword(), userRequestModel.getConfirmPassword());
        String encryptedPassword = passwordEncoder.encode(userRequestModel.getPassword());
        userRequestModel.setPassword(encryptedPassword);
        User user = modelMapper.map(userRequestModel, User.class);
        addDefaultRoles(user);
        return modelMapper.map(userRepository.save(user), UserResponseModel.class);
    }

    private void addDefaultRoles(User user) {
        Role userRole = roleRepository.findByRoleType(RoleType.USER);
        user.addRole(userRole);
        userRole.addUser(user);
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByRoleType(RoleType.ADMIN);
            user.addRole(adminRole);
            adminRole.addUser(user);
        }
    }

    @Override
    public UserResponseModel getByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return modelMapper.map(user, UserResponseModel.class);
    }

    private void validatePasswords(String password, String confirmPassword) {
        if (!Objects.equals(password, confirmPassword)) {
            throw new TRException("Passwords do not match");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}
