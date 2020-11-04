package com.tracker.taskstracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tracker.taskstracker.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);

}
