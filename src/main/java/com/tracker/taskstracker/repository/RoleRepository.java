package com.tracker.taskstracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tracker.taskstracker.domain.Role;
import com.tracker.taskstracker.domain.enums.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Role findByRoleType(RoleType roleType);
}
