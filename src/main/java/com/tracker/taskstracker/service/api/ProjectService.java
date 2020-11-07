package com.tracker.taskstracker.service.api;

import java.util.List;

import com.tracker.taskstracker.model.request.ProjectRequestModel;
import com.tracker.taskstracker.model.request.UserToProjectRequestModel;
import com.tracker.taskstracker.model.response.ProjectResponseModel;

public interface ProjectService extends GenericService<ProjectRequestModel, ProjectResponseModel, String> {

    ProjectResponseModel addUserToProject(UserToProjectRequestModel userToProjectRequestModel);

    List<ProjectResponseModel> getProjectsByUsername(String username);
}
