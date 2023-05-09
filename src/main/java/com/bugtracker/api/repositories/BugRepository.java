package com.bugtracker.api.repositories;

import com.bugtracker.api.entities.Project;
import com.bugtracker.api.entities.Bug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BugRepository extends JpaRepository<Bug, Long> {

    Page<Bug> findByProject(Project project, Pageable pageable);
}
