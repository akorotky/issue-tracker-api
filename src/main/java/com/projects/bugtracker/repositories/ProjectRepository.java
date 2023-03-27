package com.projects.bugtracker.repositories;

import com.projects.bugtracker.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
