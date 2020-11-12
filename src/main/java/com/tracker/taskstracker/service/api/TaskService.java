package com.tracker.taskstracker.service.api;

import java.util.List;

import com.tracker.taskstracker.model.request.TaskRequestModel;
import com.tracker.taskstracker.model.request.TaskStateRequestModel;
import com.tracker.taskstracker.model.request.TaskUpdateRequestModel;
import com.tracker.taskstracker.model.response.TaskResponseModel;

public interface TaskService extends GenericService<TaskRequestModel, TaskResponseModel, String> {

    List<TaskResponseModel> findTasksByProjectId(String projectId);

    TaskResponseModel findTaskExtendedById(String taskId);

    TaskResponseModel assignTaskToUser(String taskId, String username);

    TaskResponseModel alterTaskState(String taskId, TaskStateRequestModel taskStateRequestModel);

    TaskResponseModel updateTask(TaskUpdateRequestModel taskUpdateRequestModel, String taskId);

    TaskResponseModel deleteTaskById(String taskId);

}
