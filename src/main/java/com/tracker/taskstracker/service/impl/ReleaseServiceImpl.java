package com.tracker.taskstracker.service.impl;

import com.tracker.taskstracker.domain.Project;
import com.tracker.taskstracker.domain.Release;
import com.tracker.taskstracker.domain.Task;
import com.tracker.taskstracker.exception.TRException;
import com.tracker.taskstracker.model.request.ReleaseRequestModel;
import com.tracker.taskstracker.model.response.ReleaseResponseModel;
import com.tracker.taskstracker.repository.ProjectRepository;
import com.tracker.taskstracker.repository.ReleaseRepository;
import com.tracker.taskstracker.repository.TaskRepository;
import com.tracker.taskstracker.service.api.ReleaseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReleaseServiceImpl extends GenericServiceImpl<Release, ReleaseRequestModel, ReleaseResponseModel, String> implements ReleaseService {

    private final ReleaseRepository releaseRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ReleaseServiceImpl(ReleaseRepository releaseRepository, ModelMapper modelMapper, ProjectRepository projectRepository, TaskRepository taskRepository) {
        super(releaseRepository, modelMapper);
        this.releaseRepository = releaseRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public ReleaseResponseModel save(ReleaseRequestModel releaseRequestModel) {
        Project project = projectRepository.findById(releaseRequestModel.getProjectId())
                .orElseThrow(() -> new TRException("Project not found"));
        Release release = modelMapper.map(releaseRequestModel, Release.class);
        release.setProject(project);
        project.addRelease(release);
        List<Task> completedTasks = project.getCompletedTasks();
        completedTasks.forEach(task -> task.setRelease(release));
        project.setTasks(completedTasks);
        projectRepository.save(project);
        taskRepository.saveAll(completedTasks);
        return modelMapper.map(releaseRepository.save(release), ReleaseResponseModel.class);
    }

    @Override
    public List<ReleaseResponseModel> findReleasesByProjectId(String projectId) {
        List<Release> releases = releaseRepository.findAllByProjectId(projectId);
        return releases.stream()
                .map(release -> modelMapper.map(release, ReleaseResponseModel.class))
                .collect(Collectors.toList());
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
