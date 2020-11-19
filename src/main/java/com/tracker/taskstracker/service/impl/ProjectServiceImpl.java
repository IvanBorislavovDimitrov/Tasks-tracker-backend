package com.tracker.taskstracker.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tracker.taskstracker.domain.Project;
import com.tracker.taskstracker.domain.User;
import com.tracker.taskstracker.exception.TRException;
import com.tracker.taskstracker.model.request.ProjectRequestModel;
import com.tracker.taskstracker.model.request.UserToProjectRequestModel;
import com.tracker.taskstracker.model.response.ProjectResponseModel;
import com.tracker.taskstracker.model.response.ProjectResponseModelExtended;
import com.tracker.taskstracker.repository.ProjectRepository;
import com.tracker.taskstracker.repository.UserRepository;
import com.tracker.taskstracker.service.api.FileService;
import com.tracker.taskstracker.service.api.ProjectService;

@Service
public class ProjectServiceImpl extends GenericServiceImpl<Project, ProjectRequestModel, ProjectResponseModel, String>
    implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, ModelMapper modelMapper, UserRepository userRepository,
                              FileService fileService) {
        super(projectRepository, modelMapper);
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.fileService = fileService;
    }

    @Override
    public ProjectResponseModel save(ProjectRequestModel projectRequestModel) {
        String pictureName = projectRequestModel.getName() + getFileExtension(projectRequestModel.getPicture());
        modelMapper.addMappings(new PropertyMap<ProjectRequestModel, Project>() {
            @Override
            protected void configure() {
                map().setPictureName(pictureName);
            }
        });
        fileService.save(projectRequestModel.getName(), projectRequestModel.getPicture());
        return super.save(projectRequestModel);
    }

    private String getFileExtension(MultipartFile file) {
        return file.getOriginalFilename()
                   .substring(file.getOriginalFilename()
                                  .lastIndexOf("."));
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
    protected Class<ProjectResponseModel> getOutputModelClass() {
        return ProjectResponseModel.class;
    }

    @Override
    protected Class<Project> getEntityClass() {
        return Project.class;
    }
}
