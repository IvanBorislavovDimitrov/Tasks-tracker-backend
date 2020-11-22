package com.tracker.taskstracker.service.api;

import com.tracker.taskstracker.model.request.TaskRequestModel;
import com.tracker.taskstracker.model.request.TaskStateRequestModel;
import com.tracker.taskstracker.model.request.TaskUpdateRequestModel;
import com.tracker.taskstracker.model.response.TaskResponseModel;

import java.util.List;

public interface TaskService extends GenericService<TaskRequestModel, TaskResponseModel, String> {

    List<TaskResponseModel> findNonReleasedTasksByProjectId(String projectId);

    TaskResponseModel findTaskExtendedById(String taskId);

    TaskResponseModel assignTaskToUser(String taskId, String username);

    TaskResponseModel alterTaskState(String taskId, TaskStateRequestModel taskStateRequestModel);

    TaskResponseModel updateTask(TaskUpdateRequestModel taskUpdateRequestModel, String taskId);

    TaskResponseModel deleteTaskById(String taskId);

    List<TaskResponseModel> getTaskByUsername(String username);

}
