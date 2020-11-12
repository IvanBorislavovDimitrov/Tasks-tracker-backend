package com.tracker.taskstracker.service.api;

import com.tracker.taskstracker.model.request.CommentRequestModel;
import com.tracker.taskstracker.model.request.CommentUpdateRequestModel;
import com.tracker.taskstracker.model.request.ProjectCommentRequestModel;
import com.tracker.taskstracker.model.request.TaskCommentRequestModel;
import com.tracker.taskstracker.model.response.CommentResponseModel;
import com.tracker.taskstracker.model.response.ProjectCommentResponseModel;
import com.tracker.taskstracker.model.response.TaskCommentResponseModel;

public interface CommentService extends GenericService<CommentRequestModel, CommentResponseModel, String> {

    ProjectCommentResponseModel save(ProjectCommentRequestModel projectCommentRequestModel, String authorName);

    TaskCommentResponseModel save(TaskCommentRequestModel taskCommentRequestModel, String authorName);

    ProjectCommentResponseModel updateProjectComment(CommentUpdateRequestModel commentUpdateRequestModel, String commentId);

    ProjectCommentResponseModel deleteProjectCommentById(String commentId);
}
