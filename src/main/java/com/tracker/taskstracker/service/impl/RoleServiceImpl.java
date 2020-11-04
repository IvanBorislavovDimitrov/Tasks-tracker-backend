package com.tracker.taskstracker.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tracker.taskstracker.domain.Role;
import com.tracker.taskstracker.domain.enums.RoleType;
import com.tracker.taskstracker.repository.RoleRepository;
import com.tracker.taskstracker.service.api.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initializeRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(getDefaultRoles());
        }
    }

    private List<Role> getDefaultRoles() {
        Role admin = new Role();
        admin.setRoleType(RoleType.ADMIN);
        Role user = new Role();
        user.setRoleType(RoleType.USER);
        return List.of(admin, user);
    }
}
