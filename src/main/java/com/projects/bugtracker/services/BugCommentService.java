package com.projects.bugtracker.services;

import com.projects.bugtracker.dto.bugcommentdto.BugCommentRequestDto;
import com.projects.bugtracker.entities.BugComment;
import com.projects.bugtracker.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BugCommentService {

    Page<BugComment> findAllBugComments(Pageable pageable);

    BugComment findBugCommentById(Long id);

    Page<BugComment> findAllCommentsByBugId(Long bugId, Pageable pageable);

    void createBugComment(BugCommentRequestDto bugCommentRequestDto, User user);

    void updateBugComment(BugCommentRequestDto bugCommentRequestDto);

    void deleteBugCommentById(Long id);
}
