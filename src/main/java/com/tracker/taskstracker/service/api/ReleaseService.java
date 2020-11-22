package com.tracker.taskstracker.service.api;

import com.tracker.taskstracker.model.request.ReleaseRequestModel;
import com.tracker.taskstracker.model.response.ReleaseResponseModel;

import java.util.List;

public interface ReleaseService extends GenericService<ReleaseRequestModel, ReleaseResponseModel, String> {

    List<ReleaseResponseModel> findReleasesByProjectId(String projectId);
}
