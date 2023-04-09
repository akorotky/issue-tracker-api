package com.projects.bugtracker.repositories;

import com.projects.bugtracker.entities.Project;
import com.projects.bugtracker.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByOwner(User owner);
}
