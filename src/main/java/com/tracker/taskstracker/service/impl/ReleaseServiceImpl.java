package com.tracker.taskstracker.service.impl;

import com.tracker.taskstracker.domain.Project;
import com.tracker.taskstracker.domain.Release;
import com.tracker.taskstracker.model.request.ReleaseRequestModel;
import com.tracker.taskstracker.model.response.ReleaseResponseModel;
import com.tracker.taskstracker.repository.ProjectRepository;
import com.tracker.taskstracker.repository.ReleaseRepository;
import com.tracker.taskstracker.service.api.ReleaseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReleaseServiceImpl extends GenericServiceImpl<Release, ReleaseRequestModel, ReleaseResponseModel, String> implements ReleaseService {

    private final ReleaseRepository releaseRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public ReleaseServiceImpl(ReleaseRepository releaseRepository, ModelMapper modelMapper, ProjectRepository projectRepository) {
        super(releaseRepository, modelMapper);
        this.releaseRepository = releaseRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public ReleaseResponseModel save(ReleaseRequestModel releaseRequestModel) {
        Project project = projectRepository.findByName(releaseRequestModel.getProjectName());
        Release release = modelMapper.map(releaseRequestModel, Release.class);
        release.setProject(project);
        project.addRelease(release);
        return modelMapper.map(releaseRepository.save(release), ReleaseResponseModel.class);
    }

    @Override
    protected Class<ReleaseResponseModel> getOutputModelClass() {
        return ReleaseResponseModel.class;
    }

    @Override
    protected Class<Release> getEntityClass() {
        return Release.class;
    }
}
