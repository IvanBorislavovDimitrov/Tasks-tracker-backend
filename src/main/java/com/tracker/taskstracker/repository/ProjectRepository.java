package com.tracker.taskstracker.repository;

import com.tracker.taskstracker.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, String> {

    Project findByName(String name);

    List<Project> findAllByUsersUsername(String username);

}
