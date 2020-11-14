package com.tracker.taskstracker.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tracker.taskstracker.model.request.ProjectRequestModel;
import com.tracker.taskstracker.model.request.UserToProjectRequestModel;
import com.tracker.taskstracker.model.response.ProjectResponseModel;
import com.tracker.taskstracker.service.api.ProjectService;
import com.tracker.taskstracker.util.LoggedUserGetter;

@RestController
@RequestMapping(value = "/projects", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

    private final ProjectService projectService;
    private final LoggedUserGetter loggedUserGetter;

    @Autowired
    public ProjectController(ProjectService projectService, LoggedUserGetter loggedUserGetter) {
        this.projectService = projectService;
        this.loggedUserGetter = loggedUserGetter;
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseModel>> getAllProjects() {
        List<ProjectResponseModel> projectResponseModels = projectService.findAll();
        return ResponseEntity.ok(projectResponseModels);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectResponseModel> create(@Valid @ModelAttribute ProjectRequestModel projectRequestModel) {
        return ResponseEntity.ok(projectService.save(projectRequestModel));
    }

    @PostMapping(value = "/add-user-to-project")
    public ResponseEntity<ProjectResponseModel> addUserToProject(@Valid @RequestBody UserToProjectRequestModel userToProjectRequestModel) {
        ProjectResponseModel projectResponseModel = projectService.addUserToProject(userToProjectRequestModel);
        return ResponseEntity.ok(projectResponseModel);
    }

    @GetMapping(value = "/my-projects")
    public ResponseEntity<List<ProjectResponseModel>> getMyProjects() {
        List<ProjectResponseModel> projectResponseModels = projectService.findProjectsByUsername(loggedUserGetter.getUsername());
        return ResponseEntity.ok(projectResponseModels);
    }

    @GetMapping(value = "/{projectId}")
    public ResponseEntity<? extends ProjectResponseModel> getProjectById(@PathVariable String projectId) {
        ProjectResponseModel projectResponseModel = projectService.findProjectById(projectId);
        return ResponseEntity.ok(projectResponseModel);
    }
}
