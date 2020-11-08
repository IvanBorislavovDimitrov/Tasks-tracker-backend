package com.tracker.taskstracker.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.tracker.taskstracker.model.request.ProjectRequestModel;
import com.tracker.taskstracker.model.request.UserToProjectRequestModel;
import com.tracker.taskstracker.model.response.ProjectResponseModel;
import com.tracker.taskstracker.service.api.ProjectService;

@RestController
@RequestMapping(value = "/projects", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
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
        Object principal = SecurityContextHolder.getContext()
                                                .getAuthentication()
                                                .getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        List<ProjectResponseModel> projectResponseModels = projectService.findProjectsByUsername(username);
        return ResponseEntity.ok(projectResponseModels);
    }

    @GetMapping(value = "/{projectId}")
    public ResponseEntity<? extends ProjectResponseModel> getProjectById(@PathVariable String projectId) {
        ProjectResponseModel projectResponseModel = projectService.findProjectById(projectId);
        return ResponseEntity.ok(projectResponseModel);
    }
}
