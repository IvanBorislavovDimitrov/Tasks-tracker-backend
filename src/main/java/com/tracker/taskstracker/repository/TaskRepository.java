package com.tracker.taskstracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tracker.taskstracker.domain.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

}
