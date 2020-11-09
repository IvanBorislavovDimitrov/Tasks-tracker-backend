package com.tracker.taskstracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tracker.taskstracker.service.api.FileService;
import com.tracker.taskstracker.service.api.ProjectService;

@Controller
@RequestMapping(value = "/resources")
public class ResourceController {

    private final ProjectService projectService;
    private final FileService fileService;

    @Autowired
    public ResourceController(ProjectService projectService, FileService fileService) {
        this.projectService = projectService;
        this.fileService = fileService;
    }

    @GetMapping(value = "/projects/{projectId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getProjectImage(@PathVariable String projectId) {
        String projectPictureName = projectService.findProjectPictureName(projectId);
        return fileService.findImageByName(projectPictureName);
    }
}
