package com.tracker.taskstracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tracker.taskstracker.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, String> {


}
