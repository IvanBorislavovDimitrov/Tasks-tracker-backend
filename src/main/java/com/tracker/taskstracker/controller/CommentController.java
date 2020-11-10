package com.tracker.taskstracker.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.taskstracker.model.request.ProjectCommentRequestModel;
import com.tracker.taskstracker.model.request.TaskCommentRequestModel;
import com.tracker.taskstracker.model.response.ProjectCommentResponseModel;
import com.tracker.taskstracker.model.response.TaskCommentResponseModel;
import com.tracker.taskstracker.service.api.CommentService;
import com.tracker.taskstracker.util.LoggedUserGetter;

@RestController
@RequestMapping(value = "/comments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {

    private final CommentService commentService;
    private final LoggedUserGetter loggedUserGetter;

    @Autowired
    public CommentController(CommentService commentService, LoggedUserGetter loggedUserGetter) {
        this.commentService = commentService;
        this.loggedUserGetter = loggedUserGetter;
    }

    @PostMapping(value = "/project")
    public ResponseEntity<ProjectCommentResponseModel>
           createProjectComment(@Valid @RequestBody ProjectCommentRequestModel projectCommentRequestModel) {
        ProjectCommentResponseModel projectCommentResponseModel = commentService.save(projectCommentRequestModel,
                                                                                      loggedUserGetter.getUsername());
        return ResponseEntity.ok(projectCommentResponseModel);
    }

    @PostMapping(value = "/task")
    public ResponseEntity<TaskCommentResponseModel> createTaskComment(@Valid @RequestBody TaskCommentRequestModel taskCommentRequestModel) {
        TaskCommentResponseModel taskCommentResponseModel = commentService.save(taskCommentRequestModel, loggedUserGetter.getUsername());
        return ResponseEntity.ok(taskCommentResponseModel);
    }
}