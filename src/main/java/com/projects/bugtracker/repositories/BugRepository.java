package com.projects.bugtracker.repositories;

import com.projects.bugtracker.models.Bug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BugRepository extends JpaRepository<Bug, Long> {
}
