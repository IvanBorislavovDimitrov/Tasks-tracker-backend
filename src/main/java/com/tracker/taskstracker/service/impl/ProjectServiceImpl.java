package com.tracker.taskstracker.service.impl;

import com.tracker.taskstracker.domain.Project;
import com.tracker.taskstracker.domain.User;
import com.tracker.taskstracker.exception.TRException;
import com.tracker.taskstracker.model.request.ProjectRequestModel;
import com.tracker.taskstracker.model.request.UserToProjectRequestModel;
import com.tracker.taskstracker.model.response.*;
import com.tracker.taskstracker.repository.ProjectRepository;
import com.tracker.taskstracker.repository.UserRepository;
import com.tracker.taskstracker.service.api.ProjectService;
import com.tracker.taskstracker.storage.FileService;
import com.tracker.taskstracker.storage.FileStorageGetter;
import com.tracker.taskstracker.util.FilesUtil;
import org.modelmapper.ModelMapper;
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
        String pictureName = saveProjectPicture(projectRequestModel);
        Project project = modelMapper.map(projectRequestModel, Project.class);
        project.setPictureName(pictureName);
        return modelMapper.map(projectRepository.save(project), ProjectResponseModel.class);
    }

    private String saveProjectPicture(ProjectRequestModel projectRequestModel) {
        String pictureName = projectRequestModel.getName() + FilesUtil.getFileExtension(projectRequestModel.getPicture());
        fileService.save(pictureName, projectRequestModel.getPicture());
        return pictureName;
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
    public ProjectResponseModel update(String projectId, ProjectRequestModel projectRequestModel) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        String pictureName = saveProjectPicture(projectRequestModel);
        project.setPictureName(pictureName);
        project.setName(projectRequestModel.getName());
        project.setDescription(projectRequestModel.getDescription());
        return modelMapper.map(projectRepository.save(project), ProjectResponseModel.class);
    }

    @Override
    public BacklogsBugsResponseModel findBacklogsBugsCount(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        BacklogsBugsResponseModel backlogsBugsResponseModel = new BacklogsBugsResponseModel();
        backlogsBugsResponseModel.setBacklogsCount(project.getBacklogs().size());
        backlogsBugsResponseModel.setBugsCount(project.getBugs().size());
        return backlogsBugsResponseModel;
    }

    @Override
    public ProjectTaskStatesResponseModel findProjectTasksStatesStatistics(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        return ProjectTaskStatesResponseModel.builder()
                .setBacklogItems(project.getBacklogTasks().size())
                .setBlockedItems(project.getBlockedItems().size())
                .setCompletedItems(project.getCompletedTasks().size())
                .setInProgressItems(project.getInProgressTasks().size())
                .setSelectedItems(project.getSelectedTasks().size())
                .build();
    }

    @Override
    public AdminsUsersResponseModel findAdminsUsersStatistics(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        AdminsUsersResponseModel adminsUsersResponseModel = new AdminsUsersResponseModel();
        adminsUsersResponseModel.setAdminsCount(project.getAdmins().size());
        adminsUsersResponseModel.setUsersCount(project.getOnlyUsers().size());
        return adminsUsersResponseModel;
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
