package com.tracker.taskstracker.repository;

import com.tracker.taskstracker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByAccountVerificationCode(String accountVerificationCode);

    Optional<User> findByEmail(String email);

    Optional<User> findByForgottenPasswordToken(String forgottenPasswordToken);

}
