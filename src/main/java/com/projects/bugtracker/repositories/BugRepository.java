package com.projects.bugtracker.repositories;

import com.projects.bugtracker.entities.Bug;
import com.projects.bugtracker.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BugRepository extends JpaRepository<Bug, Long> {

    List<Bug> findAllByProject(Project project);
}
