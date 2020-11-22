package com.tracker.taskstracker.repository;

import com.tracker.taskstracker.domain.Release;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReleaseRepository extends JpaRepository<Release, String> {

    List<Release> findAllByProjectId(String projectId);
}
