package com.tracker.taskstracker.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tracker.taskstracker.model.request.TaskRequestModel;
import com.tracker.taskstracker.model.request.TaskStateRequestModel;
import com.tracker.taskstracker.model.request.TaskUpdateRequestModel;
import com.tracker.taskstracker.model.response.TaskResponseModel;
import com.tracker.taskstracker.service.api.TaskService;
import com.tracker.taskstracker.util.LoggedUserGetter;

@RestController
@RequestMapping(value = "/tasks", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {

    private final TaskService taskService;
    private final LoggedUserGetter loggedUserGetter;

    @Autowired
    public TaskController(TaskService taskService, LoggedUserGetter loggedUserGetter) {
        this.taskService = taskService;
        this.loggedUserGetter = loggedUserGetter;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<TaskResponseModel> createTask(@Valid @RequestBody TaskRequestModel taskRequestModel) {
        TaskResponseModel taskResponseModel = taskService.save(taskRequestModel);
        return ResponseEntity.ok(taskResponseModel);
    }

    @GetMapping(value = "/project/{projectId}")
    public ResponseEntity<List<TaskResponseModel>> getTasksByProjectId(@PathVariable String projectId) {
        List<TaskResponseModel> taskResponseModels = taskService.findTasksByProjectId(projectId);
        return ResponseEntity.ok(taskResponseModels);
    }

    @GetMapping(value = "/{taskId}")
    public ResponseEntity<TaskResponseModel> getExtendedTaskById(@PathVariable String taskId) {
        TaskResponseModel taskResponseModel = taskService.findTaskExtendedById(taskId);
        return ResponseEntity.ok(taskResponseModel);
    }

    @PostMapping(value = "/assign/{taskId}")
    public ResponseEntity<TaskResponseModel> assignToMe(@PathVariable String taskId) {
        String username = loggedUserGetter.getUsername();
        TaskResponseModel taskResponseModel = taskService.assignTaskToUser(taskId, username);
        return ResponseEntity.ok(taskResponseModel);
    }

    @PatchMapping(value = "/alter-state/{taskId}")
    public ResponseEntity<TaskResponseModel> alterTaskState(@Valid @RequestBody TaskStateRequestModel taskStateRequestModel,
                                                            @PathVariable String taskId) {
        TaskResponseModel taskResponseModel = taskService.alterTaskState(taskId, taskStateRequestModel);
        return ResponseEntity.ok(taskResponseModel);
    }

    @PatchMapping(value = "update/{taskId}")
    public ResponseEntity<TaskResponseModel> updateTask(@Valid @RequestBody TaskUpdateRequestModel taskUpdateRequestModel,
                                                        @PathVariable String taskId) {
        TaskResponseModel taskResponseModel = taskService.updateTask(taskUpdateRequestModel, taskId);
        return ResponseEntity.ok(taskResponseModel);    

    }

}
