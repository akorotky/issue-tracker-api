package com.projects.bugtracker.repositories;

import com.projects.bugtracker.entities.Bug;
import com.projects.bugtracker.entities.BugComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BugCommentRepository extends JpaRepository<BugComment, Long> {

    Page<BugComment> findByBug(Bug bug, Pageable pageable);
}
