package com.tracker.taskstracker.repository;

import com.tracker.taskstracker.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    List<Task> findAllByProjectIdAndReleaseIsNull(String projectId);

    List<Task> findAllByAssigneeUsername(String username);

    List<Task> findAllByReleaseId(String releaseId);
}
