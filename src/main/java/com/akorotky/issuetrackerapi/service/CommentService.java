package com.akorotky.issuetrackerapi.service;

import com.akorotky.issuetrackerapi.dto.comment.CommentDtoMapper;
import com.akorotky.issuetrackerapi.dto.comment.CommentRequestDto;
import com.akorotky.issuetrackerapi.entity.Issue;
import com.akorotky.issuetrackerapi.entity.Comment;
import com.akorotky.issuetrackerapi.entity.User;
import com.akorotky.issuetrackerapi.exception.ResourceNotFoundException;
import com.akorotky.issuetrackerapi.repository.CommentRepository;
import com.akorotky.issuetrackerapi.security.acl.AclPermissionService;
import com.akorotky.issuetrackerapi.security.expressions.permissions.issue.IssueReadPermission;
import com.akorotky.issuetrackerapi.security.expressions.permissions.comment.CommentAuthorPermission;
import com.akorotky.issuetrackerapi.security.expressions.permissions.comment.CommentCreatePermission;
import com.akorotky.issuetrackerapi.security.expressions.permissions.comment.CommentReadByIdPermission;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentDtoMapper commentDtoMapper;
    private final AclPermissionService aclPermissionService;
    private final Permission[] commentAuthorPermissions = {BasePermission.ADMINISTRATION};

    @IsUser
    @Transactional(readOnly = true)
    public Page<Comment> findAllComments(Pageable pageable) {
        Page<Comment> comments = commentRepository.findAll(pageable);
        if (comments.isEmpty()) throw new ResourceNotFoundException("No comments found");
        return comments;
    }

    @IsUser
    @CommentReadByIdPermission
    @Transactional(readOnly = true)
    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).
                orElseThrow(() -> new ResourceNotFoundException("Comment with id=" + commentId + " not found"));
    }

    @IssueReadPermission
    @Transactional(readOnly = true)
    public Page<Comment> findAllCommentsByIssue(Issue issue, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByIssue(issue, pageable);
        if (comments.isEmpty())
            throw new ResourceNotFoundException("No comments for issue with id=" + issue.getId() + " found");
        return comments;
    }

    @CommentCreatePermission
    public Comment createComment(Issue issue, CommentRequestDto commentRequestDto, User user) {
        Comment comment = commentDtoMapper.toEntity(commentRequestDto);
        comment.setAuthor(user);
        comment.setIssue(issue);
        commentRepository.saveAndFlush(comment);
        aclPermissionService.grantPermissions(issue, issue.getId(), user.getUsername(), commentAuthorPermissions);
        return comment;
    }

    @CommentAuthorPermission
    public void updateComment(Comment comment, CommentRequestDto commentRequestDto) {
        commentRepository.save(commentDtoMapper.toEntity(commentRequestDto));
    }

    @CommentAuthorPermission
    public void deleteComment(Comment comment) {
        commentRepository.deleteById(comment.getId());
    }
}
