package com.tracker.taskstracker.service.api;

import com.tracker.taskstracker.model.request.ProjectRequestModel;
import com.tracker.taskstracker.model.request.UserToProjectRequestModel;
import com.tracker.taskstracker.model.response.AdminsUsersResponseModel;
import com.tracker.taskstracker.model.response.BacklogsBugsResponseModel;
import com.tracker.taskstracker.model.response.ProjectResponseModel;
import com.tracker.taskstracker.model.response.ProjectTaskStatesResponseModel;

import java.util.List;

public interface ProjectService extends GenericService<ProjectRequestModel, ProjectResponseModel, String> {

    ProjectResponseModel addUserToProject(UserToProjectRequestModel userToProjectRequestModel);

    List<ProjectResponseModel> findProjectsByUsername(String username);

    ProjectResponseModel findProjectById(String projectId);

    String findProjectPictureName(String projectId);

    BacklogsBugsResponseModel findBacklogsBugsCount(String projectId);

    ProjectTaskStatesResponseModel findProjectTasksStatesStatistics(String projectId);

    AdminsUsersResponseModel findAdminsUsersStatistics(String projectId);
}
