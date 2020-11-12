package com.tracker.taskstracker.service.api;

import java.util.List;

import com.tracker.taskstracker.model.request.TaskRequestModel;
import com.tracker.taskstracker.model.response.TaskResponseModel;

public interface TaskService extends GenericService<TaskRequestModel, TaskResponseModel, String> {

    List<TaskResponseModel> findTasksByProjectId(String projectId);

    TaskResponseModel findTaskExtendedById(String taskId);
}
