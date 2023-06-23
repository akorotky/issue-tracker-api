package com.bugtracker.api.repository;

import com.bugtracker.api.entity.BugComment;
import com.bugtracker.api.entity.Bug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BugCommentRepository extends JpaRepository<BugComment, Long> {

    Page<BugComment> findByBug(Bug bug, Pageable pageable);
}
