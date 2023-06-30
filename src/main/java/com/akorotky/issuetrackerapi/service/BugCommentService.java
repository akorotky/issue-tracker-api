package com.akorotky.issuetrackerapi.service;

import com.akorotky.issuetrackerapi.dto.bugcomment.BugCommentDtoMapper;
import com.akorotky.issuetrackerapi.dto.bugcomment.BugCommentRequestDto;
import com.akorotky.issuetrackerapi.entity.Bug;
import com.akorotky.issuetrackerapi.entity.BugComment;
import com.akorotky.issuetrackerapi.entity.User;
import com.akorotky.issuetrackerapi.exception.ResourceNotFoundException;
import com.akorotky.issuetrackerapi.repository.BugCommentRepository;
import com.akorotky.issuetrackerapi.security.acl.AclPermissionService;
import com.akorotky.issuetrackerapi.security.expressions.permissions.bug.BugReadPermission;
import com.akorotky.issuetrackerapi.security.expressions.permissions.bugcomment.BugCommentAuthorPermission;
import com.akorotky.issuetrackerapi.security.expressions.permissions.bugcomment.BugCommentCreatePermission;
import com.akorotky.issuetrackerapi.security.expressions.permissions.bugcomment.BugCommentReadByIdPermission;
import com.akorotky.issuetrackerapi.security.expressions.permissions.role.IsUser;
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
public class BugCommentService {

    private final BugCommentRepository bugCommentRepository;
    private final BugCommentDtoMapper bugCommentDtoMapper;
    private final AclPermissionService aclPermissionService;
    private final Permission[] commentAuthorPermissions = {BasePermission.ADMINISTRATION};

    @IsUser
    @Transactional(readOnly = true)
    public Page<BugComment> findAllBugComments(Pageable pageable) {
        Page<BugComment> bugComments = bugCommentRepository.findAll(pageable);
        if (bugComments.isEmpty()) throw new ResourceNotFoundException("No bug comments found");
        return bugComments;
    }

    @IsUser
    @BugCommentReadByIdPermission
    @Transactional(readOnly = true)
    public BugComment findBugCommentById(Long commentId) {
        return bugCommentRepository.findById(commentId).
                orElseThrow(() -> new ResourceNotFoundException("Bug comment with id=" + commentId + " not found"));
    }

    @BugReadPermission
    @Transactional(readOnly = true)
    public Page<BugComment> findAllCommentsByBug(Bug bug, Pageable pageable) {
        Page<BugComment> bugComments = bugCommentRepository.findByBug(bug, pageable);
        if (bugComments.isEmpty())
            throw new ResourceNotFoundException("No comments for bug with id=" + bug.getId() + " found");
        return bugComments;
    }

    @BugCommentCreatePermission
    public BugComment createBugComment(Bug bug, BugCommentRequestDto bugCommentRequestDto, User user) {
        BugComment bugComment = bugCommentDtoMapper.toEntity(bugCommentRequestDto);
        bugComment.setAuthor(user);
        bugComment.setBug(bug);
        bugCommentRepository.saveAndFlush(bugComment);
        aclPermissionService.grantPermissions(bug, bug.getId(), user.getUsername(), commentAuthorPermissions);
        return bugComment;
    }

    @BugCommentAuthorPermission
    public void updateBugComment(BugComment bugComment, BugCommentRequestDto bugCommentRequestDto) {
        bugCommentRepository.save(bugCommentDtoMapper.toEntity(bugCommentRequestDto));
    }

    @BugCommentAuthorPermission
    public void deleteBugComment(BugComment bugComment) {
        bugCommentRepository.deleteById(bugComment.getId());
    }
}
