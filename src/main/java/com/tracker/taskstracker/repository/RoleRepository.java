package com.tracker.taskstracker.repository;

import com.tracker.taskstracker.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Role findByType(Role.Type type);
}
