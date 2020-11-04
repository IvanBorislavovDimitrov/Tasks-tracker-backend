package com.tracker.taskstracker.service.impl;

import com.tracker.taskstracker.model.request.ProjectRequestModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.tracker.taskstracker.domain.Project;
import com.tracker.taskstracker.model.response.ProjectResponseModel;
import com.tracker.taskstracker.repository.ProjectRepository;
import com.tracker.taskstracker.service.api.ProjectService;

@Service
public class ProjectServiceImpl extends GenericServiceImpl<Project, ProjectRequestModel, ProjectResponseModel, String> implements ProjectService {

    protected ProjectServiceImpl(ProjectRepository projectRepository, ModelMapper modelMapper) {
        super(projectRepository, modelMapper);
    }

    @Override
    protected Class<ProjectResponseModel> getOutputModelClass() {
        return ProjectResponseModel.class;
    }

    @Override
    protected Class<Project> getEntityClass() {
        return Project.class;
    }
}
