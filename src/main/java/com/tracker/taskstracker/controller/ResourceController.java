package com.tracker.taskstracker.controller;

import com.tracker.taskstracker.model.response.UserResponseModel;
import com.tracker.taskstracker.storage.FileService;
import com.tracker.taskstracker.service.api.ProjectService;
import com.tracker.taskstracker.service.api.UserService;
import com.tracker.taskstracker.storage.FileStorageGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/resources")
public class ResourceController {

    private final ProjectService projectService;
    private final FileService fileService;
    private final UserService userService;

    @Autowired
    public ResourceController(ProjectService projectService, FileStorageGetter fileStorageGetter, UserService userService) {
        this.projectService = projectService;
        this.fileService = fileStorageGetter.getFileService();
        this.userService = userService;
    }

    @GetMapping(value = "/projects/{projectId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getProjectImage(@PathVariable String projectId) {
        String projectPictureName = projectService.findProjectPictureName(projectId);
        return fileService.findFileByName(projectPictureName);
    }

    @GetMapping(value = "/users/profile-picture/{userId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getUserProfilePicture(@PathVariable String userId) {
        UserResponseModel userResponseModel = userService.findById(userId);
        return fileService.findFileByName(userResponseModel.getProfilePictureName());
    }
}
