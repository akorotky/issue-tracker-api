package com.bugtracker.api.services;

import com.bugtracker.api.dto.bugcommentdto.BugCommentRequestDto;
import com.bugtracker.api.entities.User;
import com.bugtracker.api.entities.BugComment;
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
