package com.bugtracker.api.services;

import com.bugtracker.api.dto.bugcomment.BugCommentRequestDto;
import com.bugtracker.api.entities.Bug;
import com.bugtracker.api.entities.User;
import com.bugtracker.api.entities.BugComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BugCommentService {

    Page<BugComment> findAllBugComments(Pageable pageable);

    BugComment findBugCommentById(Long bugCommentId);

    Page<BugComment> findAllCommentsByBug(Bug bug, Pageable pageable);

    BugComment createBugComment(Bug bug, BugCommentRequestDto bugCommentRequestDto, User user);

    void updateBugComment(BugComment bugComment, BugCommentRequestDto bugCommentRequestDto);

    void deleteBugComment(BugComment bugComment);
}
