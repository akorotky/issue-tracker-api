package com.akorotky.issuetrackerapi.repository;

import com.akorotky.issuetrackerapi.entity.Bug;
import com.akorotky.issuetrackerapi.entity.BugComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BugCommentRepository extends JpaRepository<BugComment, Long> {

    Page<BugComment> findByBug(Bug bug, Pageable pageable);
}
