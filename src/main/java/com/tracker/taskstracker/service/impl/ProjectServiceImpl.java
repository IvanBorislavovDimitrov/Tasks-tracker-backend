package com.tracker.taskstracker.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tracker.taskstracker.domain.Project;
import com.tracker.taskstracker.domain.User;
import com.tracker.taskstracker.model.request.ProjectRequestModel;
import com.tracker.taskstracker.model.request.UserToProjectRequestModel;
import com.tracker.taskstracker.model.response.ProjectResponseModel;
import com.tracker.taskstracker.repository.ProjectRepository;
import com.tracker.taskstracker.repository.UserRepository;
import com.tracker.taskstracker.service.api.ProjectService;

@Service
public class ProjectServiceImpl extends GenericServiceImpl<Project, ProjectRequestModel, ProjectResponseModel, String>
    implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, ModelMapper modelMapper, UserRepository userRepository) {
        super(projectRepository, modelMapper);
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProjectResponseModel addUserToProject(UserToProjectRequestModel userToProjectRequestModel) {
        User user = userRepository.findByUsername(userToProjectRequestModel.getUsername());
        Project project = projectRepository.findByName(userToProjectRequestModel.getProjectName());
        user.addProject(project);
        project.addUser(user);
        projectRepository.save(project);
        userRepository.save(user);
        return modelMapper.map(project, ProjectResponseModel.class);
    }

    @Override
    public List<ProjectResponseModel> getProjectsByUsername(String username) {
        List<Project> projects = projectRepository.findAllByUsersUsername(username);
        return projects.stream()
                       .map(project -> modelMapper.map(project, ProjectResponseModel.class))
                       .collect(Collectors.toList());
    }

    @Override
    protected Class<ProjectResponseModel> getOutputModelClass() {
        return ProjectResponseModel.class;
    }

    @Override
    protected Class<Project> getEntityClass() {
        return Project.class;
    }
}
