package com.projects.bugtracker.repositories;

import com.projects.bugtracker.entities.Bug;
import com.projects.bugtracker.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BugRepository extends JpaRepository<Bug, Long> {

    Page<Bug> findByProject(Project project, Pageable pageable);
}
