package com.tracker.taskstracker.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tracker.taskstracker.domain.Role;
import com.tracker.taskstracker.model.request.RoleRequestModel;
import com.tracker.taskstracker.model.response.RoleResponseModel;
import com.tracker.taskstracker.repository.RoleRepository;
import com.tracker.taskstracker.service.api.RoleService;

@Service
public class RoleServiceImpl extends GenericServiceImpl<Role, RoleRequestModel, RoleResponseModel, String> implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    protected RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        super(roleRepository, modelMapper);
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
        admin.setRoleType(Role.RoleType.ADMIN);
        Role user = new Role();
        user.setRoleType(Role.RoleType.USER);
        return List.of(admin, user);
    }

    @Override
    protected Class<RoleResponseModel> getOutputModelClass() {
        return RoleResponseModel.class;
    }

    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }
}
