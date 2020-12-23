package com.tracker.taskstracker.service.impl;

import com.tracker.taskstracker.domain.Project;
import com.tracker.taskstracker.domain.User;
import com.tracker.taskstracker.exception.TRException;
import com.tracker.taskstracker.model.request.ProjectRequestModel;
import com.tracker.taskstracker.model.request.UserToProjectRequestModel;
import com.tracker.taskstracker.model.response.ProjectResponseModel;
import com.tracker.taskstracker.model.response.ProjectResponseModelExtended;
import com.tracker.taskstracker.repository.ProjectRepository;
import com.tracker.taskstracker.repository.UserRepository;
import com.tracker.taskstracker.service.api.ProjectService;
import com.tracker.taskstracker.storage.FileService;
import com.tracker.taskstracker.storage.FileStorageGetter;
import com.tracker.taskstracker.util.FilesUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl extends GenericServiceImpl<Project, ProjectRequestModel, ProjectResponseModel, String>
        implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, ModelMapper modelMapper, UserRepository userRepository,
                              FileStorageGetter fileStorageGetter) {
        super(projectRepository, modelMapper);
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.fileService = fileStorageGetter.getFileService();
    }

    @Override
    public ProjectResponseModel save(ProjectRequestModel projectRequestModel) {
        String pictureName = projectRequestModel.getName() + FilesUtil.getFileExtension(projectRequestModel.getPicture());
        modelMapper.addMappings(new PropertyMap<ProjectRequestModel, Project>() {
            @Override
            protected void configure() {
                map().setPictureName(pictureName);
            }
        });
        fileService.save(pictureName, projectRequestModel.getPicture());
        return super.save(projectRequestModel);
    }

    @Override
    public ProjectResponseModel addUserToProject(UserToProjectRequestModel userToProjectRequestModel) {
        User user = userRepository.findByUsername(userToProjectRequestModel.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Project project = projectRepository.findByName(userToProjectRequestModel.getProjectName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        if (isUserAlreadyAdded(user, project.getName())) {
            return modelMapper.map(project, ProjectResponseModel.class);
        }
        user.addProject(project);
        project.addUser(user);
        projectRepository.save(project);
        userRepository.save(user);
        return modelMapper.map(project, ProjectResponseModel.class);
    }

    private boolean isUserAlreadyAdded(User user, String projectName) {
        return user.getProjects().stream()
                .map(Project::getName)
                .anyMatch(currentProjectName -> Objects.equals(currentProjectName, projectName));
    }

    @Override
    public List<ProjectResponseModel> findProjectsByUsername(String username) {
        List<Project> projects = projectRepository.findAllByUsersUsername(username);
        return projects.stream()
                .map(project -> modelMapper.map(project, ProjectResponseModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponseModel findProjectById(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new TRException("Project not found"));
        return modelMapper.map(project, ProjectResponseModelExtended.class);
    }

    @Override
    public String findProjectPictureName(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new TRException("Project not found"));
        return project.getPictureName();
    }

    @Override
    public ProjectResponseModel deleteById(String projectId) {
        ProjectResponseModel projectResponseModel = super.deleteById(projectId);
        fileService.deleteByName(projectResponseModel.getPictureName());
        return projectResponseModel;
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
