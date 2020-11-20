package com.tracker.taskstracker.service.impl;

import com.tracker.taskstracker.domain.Role;
import com.tracker.taskstracker.model.request.RoleRequestModel;
import com.tracker.taskstracker.model.response.RoleResponseModel;
import com.tracker.taskstracker.repository.RoleRepository;
import com.tracker.taskstracker.service.api.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoleServiceImpl extends GenericServiceImpl<Role, RoleRequestModel, RoleResponseModel, String> implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
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
        return Stream.of(Role.Type.values())
                .map(this::createRole)
                .collect(Collectors.toList());
    }

    private Role createRole(Role.Type type) {
        Role role = new Role();
        role.setType(type);
        return role;
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
