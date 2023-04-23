package com.projects.bugtracker.services;

import com.projects.bugtracker.dto.BugCommentDto;
import com.projects.bugtracker.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BugCommentService {

    Page<BugCommentDto> findAllBugComments(Pageable pageable);

    BugCommentDto findBugCommentById(Long id);

    Page<BugCommentDto> findAllCommentsByBugId(Long bugId, Pageable pageable);

    void createBugComment(BugCommentDto bugCommentDto, User user);

    void updateBugComment(BugCommentDto bugCommentDto);

    void deleteBugCommentById(Long id);
}
