package com.projects.bugtracker.repositories;

import com.projects.bugtracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
