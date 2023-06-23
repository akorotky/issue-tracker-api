package com.bugtracker.api.service;

import com.bugtracker.api.dto.bugcomment.BugCommentRequestDto;
import com.bugtracker.api.entity.Bug;
import com.bugtracker.api.entity.User;
import com.bugtracker.api.entity.BugComment;
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
