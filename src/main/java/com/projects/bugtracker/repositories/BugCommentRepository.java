package com.projects.bugtracker.BugComment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BugCommentRepository extends JpaRepository<BugComment, Long> {
}
