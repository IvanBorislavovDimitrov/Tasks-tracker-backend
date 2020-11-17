package com.tracker.taskstracker.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tracker.taskstracker.domain.Project;
import com.tracker.taskstracker.domain.Task;
import com.tracker.taskstracker.domain.User;
import com.tracker.taskstracker.exception.TRException;
import com.tracker.taskstracker.model.request.TaskRequestModel;
import com.tracker.taskstracker.model.request.TaskStateRequestModel;
import com.tracker.taskstracker.model.request.TaskUpdateRequestModel;
import com.tracker.taskstracker.model.response.TaskResponseModel;
import com.tracker.taskstracker.model.response.TaskResponseModelExtended;
import com.tracker.taskstracker.repository.ProjectRepository;
import com.tracker.taskstracker.repository.TaskRepository;
import com.tracker.taskstracker.repository.UserRepository;
import com.tracker.taskstracker.service.api.TaskService;

@Service
public class TaskServiceImpl extends GenericServiceImpl<Task, TaskRequestModel, TaskResponseModel, String> implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, ModelMapper modelMapper, ProjectRepository projectRepository,
                           UserRepository userRepository) {
        super(taskRepository, modelMapper);
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
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

    @Override
    public List<TaskResponseModel> findTasksByProjectId(String projectId) {
        List<Task> tasks = taskRepository.findAllByProjectId(projectId);
        return tasks.stream()
                    .map(task -> modelMapper.map(task, TaskResponseModel.class))
                    .collect(Collectors.toList());
    }

    private void setDefaultTaskState(Task task) {
        task.setState(Task.State.BACKLOG);
    }

    private void setDefaultDates(Task task) {
        task.setCreatedAt(new Date());
        task.setUpdatedAt(new Date());
    }

    @Override
    public TaskResponseModel findTaskExtendedById(String taskId) {
        Task task = taskRepository.findById(taskId)
                                  .orElseThrow(() -> new TRException("Task not found"));
        return modelMapper.map(task, TaskResponseModelExtended.class);
    }

    @Override
    public TaskResponseModel assignTaskToUser(String taskId, String username) {
        Task task = taskRepository.findById(taskId)
                                  .orElseThrow(() -> new TRException("Task not found"));
        User user = userRepository.findByUsername(username);
        task.setAssignee(user);
        return modelMapper.map(taskRepository.save(task), TaskResponseModel.class);
    }

    @Override
    public TaskResponseModel alterTaskState(String taskId, TaskStateRequestModel taskStateRequestModel) {
        Task task = taskRepository.findById(taskId)
                                  .orElseThrow(() -> new TRException("Task not found"));
        Task.State state = Task.State.fromString(taskStateRequestModel.getState());
        task.setState(state);
        return modelMapper.map(taskRepository.save(task), TaskResponseModel.class);
    }

    @Override
    public TaskResponseModel updateTask(TaskUpdateRequestModel taskUpdateRequestModel, String taskId) {
        Task task = taskRepository.findById(taskId)
                                  .orElseThrow(() -> new TRException("Task not found"));
        task.setName(taskUpdateRequestModel.getName());
        task.setDescription(taskUpdateRequestModel.getDescription());
        return modelMapper.map(taskRepository.save(task), TaskResponseModel.class);
    }

    @Override
    public TaskResponseModel deleteTaskById(String taskId) {
        Task task = taskRepository.findById(taskId)
                                  .orElseThrow(() -> new TRException("Task not found"));
        TaskResponseModel taskResponseModel = modelMapper.map(task, TaskResponseModel.class);
        taskRepository.delete(task);
        return taskResponseModel;
    }

    @Override
    public List<TaskResponseModel> getTaskByUsername(String username) {
        List<Task> tasksByUsername = taskRepository.findAllByAssigneeUsername(username);
        return tasksByUsername.stream()
                              .map(task -> modelMapper.map(task, TaskResponseModel.class))
                              .collect(Collectors.toList());
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
