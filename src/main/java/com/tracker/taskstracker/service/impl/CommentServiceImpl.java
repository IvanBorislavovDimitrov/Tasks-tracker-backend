package com.tracker.taskstracker.service.impl;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tracker.taskstracker.domain.*;
import com.tracker.taskstracker.exception.TRException;
import com.tracker.taskstracker.model.request.CommentRequestModel;
import com.tracker.taskstracker.model.request.ProjectCommentRequestModel;
import com.tracker.taskstracker.model.request.TaskCommentRequestModel;
import com.tracker.taskstracker.model.response.CommentResponseModel;
import com.tracker.taskstracker.model.response.ProjectCommentResponseModel;
import com.tracker.taskstracker.model.response.TaskCommentResponseModel;
import com.tracker.taskstracker.repository.CommentRepository;
import com.tracker.taskstracker.repository.ProjectRepository;
import com.tracker.taskstracker.repository.TaskRepository;
import com.tracker.taskstracker.repository.UserRepository;
import com.tracker.taskstracker.service.api.CommentService;

@Service
public class CommentServiceImpl extends GenericServiceImpl<Comment, CommentRequestModel, CommentResponseModel, String>
    implements CommentService {

    private final CommentRepository commentRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    protected CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper, CommentRepository commentRepository1,
                                 ProjectRepository projectRepository, TaskRepository taskRepository, UserRepository userRepository) {
        super(commentRepository, modelMapper);
        this.commentRepository = commentRepository1;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Class<CommentResponseModel> getOutputModelClass() {
        return CommentResponseModel.class;
    }

    @Override
    public ProjectCommentResponseModel save(ProjectCommentRequestModel projectCommentRequestModel, String authorName) {
        Project project = projectRepository.findById(projectCommentRequestModel.getProjectId())
                                           .orElseThrow(() -> new TRException("Project not found!"));
        ProjectComment projectComment = modelMapper.map(projectCommentRequestModel, ProjectComment.class);
        projectComment.setProject(project);
        updateCreatedAndUpdatedAt(projectComment);
        User author = userRepository.findByUsername(authorName);
        projectComment.setAuthor(author);
        ProjectComment savedProjectComment = commentRepository.save(projectComment);
        return modelMapper.map(savedProjectComment, ProjectCommentResponseModel.class);
    }

    private void updateCreatedAndUpdatedAt(Comment comment) {
        comment.setCreatedAt(new Date());
        comment.setUpdatedAt(new Date());
    }

    @Override
    public TaskCommentResponseModel save(TaskCommentRequestModel taskCommentRequestModel, String authorName) {
        Task task = taskRepository.findById(taskCommentRequestModel.getTaskId())
                                  .orElseThrow(() -> new TRException("Task not found!"));
        TaskComment taskComment = modelMapper.map(taskCommentRequestModel, TaskComment.class);
        taskComment.setTask(task);
        updateCreatedAndUpdatedAt(taskComment);
        User author = userRepository.findByUsername(authorName);
        taskComment.setAuthor(author);
        TaskComment savedTaskComment = commentRepository.save(taskComment);
        return modelMapper.map(savedTaskComment, TaskCommentResponseModel.class);
    }

    @Override
    protected Class<Comment> getEntityClass() {
        return Comment.class;
    }

}
