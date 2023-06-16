package com.bugtracker.api.services.impl;

import com.bugtracker.api.dto.bugcomment.BugCommentDtoMapper;
import com.bugtracker.api.dto.bugcomment.BugCommentRequestDto;
import com.bugtracker.api.entities.BugComment;
import com.bugtracker.api.entities.User;
import com.bugtracker.api.exceptions.ResourceNotFoundException;
import com.bugtracker.api.repositories.BugCommentRepository;
import com.bugtracker.api.security.acl.AclPermissionService;
import com.bugtracker.api.security.expressions.permissions.bug.BugReadPermission;
import com.bugtracker.api.security.expressions.permissions.bugcomment.BugCommentAuthorPermission;
import com.bugtracker.api.security.expressions.permissions.bugcomment.BugCommentCreatePermission;
import com.bugtracker.api.security.expressions.permissions.bugcomment.BugCommentReadByIdPermission;
import com.bugtracker.api.security.expressions.permissions.role.IsUser;
import com.bugtracker.api.services.BugCommentService;
import com.bugtracker.api.entities.Bug;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BugCommentServiceImpl implements BugCommentService {

    private final BugCommentRepository bugCommentRepository;
    private final BugCommentDtoMapper bugCommentDtoMapper;
    private final AclPermissionService aclPermissionService;
    private final Permission[] commentAuthorPermissions = {BasePermission.ADMINISTRATION};

    @IsUser
    @Transactional(readOnly = true)
    @Override
    public Page<BugComment> findAllBugComments(Pageable pageable) {
        Page<BugComment> bugComments = bugCommentRepository.findAll(pageable);
        if (bugComments.isEmpty()) throw new ResourceNotFoundException("No bug comments found");
        return bugComments;
    }

    @IsUser
    @BugCommentReadByIdPermission
    @Transactional(readOnly = true)
    @Override
    public BugComment findBugCommentById(Long commentId) {
        return bugCommentRepository.findById(commentId).
                orElseThrow(() -> new ResourceNotFoundException("Bug comment with id=" + commentId + " not found"));
    }

    @BugReadPermission
    @Transactional(readOnly = true)
    @Override
    public Page<BugComment> findAllCommentsByBug(Bug bug, Pageable pageable) {
        Page<BugComment> bugComments = bugCommentRepository.findByBug(bug, pageable);
        if (bugComments.isEmpty())
            throw new ResourceNotFoundException("No comments for bug with id=" + bug.getId() + " found");
        return bugComments;
    }

    @BugCommentCreatePermission
    @Override
    public BugComment createBugComment(Bug bug, BugCommentRequestDto bugCommentRequestDto, User user) {
        BugComment bugComment = bugCommentDtoMapper.toEntity(bugCommentRequestDto);
        bugComment.setAuthor(user);
        bugComment.setBug(bug);
        bugCommentRepository.saveAndFlush(bugComment);
        aclPermissionService.grantPermissions(bug, bug.getId(), user.getUsername(), commentAuthorPermissions);
        return bugComment;
    }

    @BugCommentAuthorPermission
    @Override
    public void updateBugComment(BugComment bugComment, BugCommentRequestDto bugCommentRequestDto) {
        bugCommentRepository.save(bugCommentDtoMapper.toEntity(bugCommentRequestDto));
    }

    @BugCommentAuthorPermission
    @Override
    public void deleteBugComment(BugComment bugComment) {
        bugCommentRepository.deleteById(bugComment.getId());
    }
}
