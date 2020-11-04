package com.tracker.taskstracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tracker.taskstracker.domain.Role;
import com.tracker.taskstracker.domain.enums.RoleType;

public interface RoleRepository extends JpaRepository<Role, String> {

    Role findByRoleType(RoleType roleType);
}
