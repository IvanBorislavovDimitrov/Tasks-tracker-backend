package com.tracker.taskstracker.repository;

import com.tracker.taskstracker.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {


}
