package com.tracker.taskstracker.service.impl;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tracker.taskstracker.domain.Project;
import com.tracker.taskstracker.domain.Task;
import com.tracker.taskstracker.model.request.TaskRequestModel;
import com.tracker.taskstracker.model.response.TaskResponseModel;
import com.tracker.taskstracker.repository.ProjectRepository;
import com.tracker.taskstracker.repository.TaskRepository;
import com.tracker.taskstracker.service.api.TaskService;

@Service
public class TaskServiceImpl extends GenericServiceImpl<Task, TaskRequestModel, TaskResponseModel, String> implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    protected TaskServiceImpl(TaskRepository taskRepository, ModelMapper modelMapper, ProjectRepository projectRepository) {
        super(taskRepository, modelMapper);
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskResponseModel save(TaskRequestModel taskRequestModel) {
        Task task = modelMapper.map(taskRequestModel, Task.class);
        setDefaultTaskState(task);
        setDefaultDates(task);
        Project project = projectRepository.findByName(taskRequestModel.getProjectName());
        task.setProject(project);
        project.addTask(task);
        taskRepository.save(task);
        return modelMapper.map(task, TaskResponseModel.class);
    }

    private void setDefaultTaskState(Task task) {
        task.setState(Task.State.BACKLOG);
    }

    private void setDefaultDates(Task task) {
        task.setCreatedAt(new Date());
        task.setUpdatedAt(new Date());
    }

    @Override
    protected Class<TaskResponseModel> getOutputModelClass() {
        return TaskResponseModel.class;
    }

    @Override
    protected Class<Task> getEntityClass() {
        return Task.class;
    }
}
