package com.project.demo.repository;

import com.project.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// This is an interface store data in DB
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
