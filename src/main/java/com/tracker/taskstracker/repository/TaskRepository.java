package com.tracker.taskstracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tracker.taskstracker.domain.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    List<Task> findAllByProjectId(String projectId);

    List<Task> findAllByAssigneeUsername(String username);
}
