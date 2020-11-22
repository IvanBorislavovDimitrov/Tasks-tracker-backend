package com.tracker.taskstracker.repository;

import com.tracker.taskstracker.domain.Release;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseRepository extends JpaRepository<Release, String> {
}
