package com.tracker.taskstracker.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.taskstracker.model.request.TaskRequestModel;
import com.tracker.taskstracker.model.response.TaskResponseModel;
import com.tracker.taskstracker.service.api.TaskService;

@RestController
@RequestMapping(value = "/tasks", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseModel> createTask(@Valid @RequestBody TaskRequestModel taskRequestModel) {
        TaskResponseModel taskResponseModel = taskService.save(taskRequestModel);
        return ResponseEntity.ok(taskResponseModel);
    }
}
