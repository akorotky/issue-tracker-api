package com.projects.bugtracker.repositories;

import com.projects.bugtracker.models.BugComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BugCommentRepository extends JpaRepository<BugComment, Long> {
}
