package com.tracker.taskstracker.service.impl;

import com.tracker.taskstracker.domain.*;
import com.tracker.taskstracker.exception.TRException;
import com.tracker.taskstracker.model.request.CommentRequestModel;
import com.tracker.taskstracker.model.request.CommentUpdateRequestModel;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Objects;

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
    public ProjectCommentResponseModel save(ProjectCommentRequestModel projectCommentRequestModel, String authorName) {
        Project project = projectRepository.findById(projectCommentRequestModel.getProjectId())
                .orElseThrow(() -> new TRException("Project not found!"));
        ProjectComment projectComment = modelMapper.map(projectCommentRequestModel, ProjectComment.class);
        projectComment.setProject(project);
        updateCreatedAndUpdatedAt(projectComment);
        User author = userRepository.findByUsername(authorName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
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
        User author = userRepository.findByUsername(authorName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        taskComment.setAuthor(author);
        TaskComment savedTaskComment = commentRepository.save(taskComment);
        return modelMapper.map(savedTaskComment, TaskCommentResponseModel.class);
    }

    @Override
    public ProjectCommentResponseModel updateProjectComment(CommentUpdateRequestModel commentUpdateRequestModel, String commentId,
                                                            String username) {
        Comment comment = updateCommentDescription(commentUpdateRequestModel, commentId, username);
        return modelMapper.map(commentRepository.save(comment), ProjectCommentResponseModel.class);
    }

    private Comment updateCommentDescription(CommentUpdateRequestModel commentUpdateRequestModel, String commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new TRException("Comment not found"));
        if (!Objects.equals(comment.getAuthor()
                        .getUsername(),
                username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        comment.setDescription(commentUpdateRequestModel.getDescription());
        comment.setUpdatedAt(new Date());
        return comment;
    }

    @Override
    public ProjectCommentResponseModel deleteProjectCommentById(String commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new TRException("Comment not found"));
        if (!Objects.equals(comment.getAuthor()
                        .getUsername(),
                username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        commentRepository.delete(comment);
        return modelMapper.map(comment, ProjectCommentResponseModel.class);
    }

    @Override
    public TaskCommentResponseModel deleteTaskCommentById(String commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new TRException("Comment not found"));
        if (!Objects.equals(comment.getAuthor()
                        .getUsername(),
                username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        commentRepository.delete(comment);
        return modelMapper.map(comment, TaskCommentResponseModel.class);
    }

    @Override
    public CommentResponseModel updateTaskProjectComment(CommentUpdateRequestModel commentUpdateRequestModel, String commentId,
                                                         String username) {
        Comment comment = updateCommentDescription(commentUpdateRequestModel, commentId, username);
        return modelMapper.map(commentRepository.save(comment), TaskCommentResponseModel.class);

    }

    @Override
    protected Class<Comment> getEntityClass() {
        return Comment.class;
    }

    @Override
    public Class<CommentResponseModel> getOutputModelClass() {
        return CommentResponseModel.class;
    }

}
