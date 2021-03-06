package com.tracker.taskstracker.controller;

import com.tracker.taskstracker.model.request.CommentUpdateRequestModel;
import com.tracker.taskstracker.model.request.ProjectCommentRequestModel;
import com.tracker.taskstracker.model.request.TaskCommentRequestModel;
import com.tracker.taskstracker.model.response.CommentResponseModel;
import com.tracker.taskstracker.model.response.ProjectCommentResponseModel;
import com.tracker.taskstracker.model.response.TaskCommentResponseModel;
import com.tracker.taskstracker.service.api.CommentService;
import com.tracker.taskstracker.util.LoggedUserGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @GetMapping(value = "/{commentId}")
    public ResponseEntity<CommentResponseModel> getProjectCommentById(@PathVariable String commentId) {
        CommentResponseModel commentResponseModel = commentService.findById(commentId);
        return ResponseEntity.ok(commentResponseModel);
    }

    @PatchMapping(value = "/update/project/{commentId}")
    public ResponseEntity<CommentResponseModel> editProjectComment(@Valid @RequestBody CommentUpdateRequestModel commentUpdateRequestModel,
                                                                   @PathVariable String commentId) {
        String username = loggedUserGetter.getUsername();
        CommentResponseModel commentResponseModel = commentService.updateProjectComment(commentUpdateRequestModel, commentId, username);
        return ResponseEntity.ok(commentResponseModel);
    }

    @PatchMapping(value = "/update/task/{commentId}")
    public ResponseEntity<CommentResponseModel> editTaskComment(@Valid @RequestBody CommentUpdateRequestModel commentUpdateRequestModel,
                                                                @PathVariable String commentId) {
        String username = loggedUserGetter.getUsername();
        CommentResponseModel commentResponseModel = commentService.updateTaskProjectComment(commentUpdateRequestModel, commentId, username);
        return ResponseEntity.ok(commentResponseModel);
    }

    @DeleteMapping(value = "/delete/project/{commentId}")
    public ResponseEntity<ProjectCommentResponseModel> deleteProjectCommentById(@PathVariable String commentId) {
        String username = loggedUserGetter.getUsername();
        ProjectCommentResponseModel commentResponseModel = commentService.deleteProjectCommentById(commentId, username);
        return ResponseEntity.ok(commentResponseModel);
    }

    @DeleteMapping(value = "/delete/task/{commentId}")
    public ResponseEntity<TaskCommentResponseModel> deleteTaskCommentById(@PathVariable String commentId) {
        String username = loggedUserGetter.getUsername();
        TaskCommentResponseModel taskCommentResponseModel = commentService.deleteTaskCommentById(commentId, username);
        return ResponseEntity.ok(taskCommentResponseModel);
    }
}
