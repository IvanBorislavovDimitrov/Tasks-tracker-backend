package com.tracker.taskstracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tracker.taskstracker.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, String> {

    Project findByName(String name);

    List<Project> findAllByUsersUsername(String username);

}
