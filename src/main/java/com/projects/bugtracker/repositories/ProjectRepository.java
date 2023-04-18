package com.projects.bugtracker.repositories;

import com.projects.bugtracker.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOwner_Username(String username);

    List<Project> findByCollaborators_Username(String username);
}
