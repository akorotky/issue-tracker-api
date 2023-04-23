package com.projects.bugtracker.dto;

import com.projects.bugtracker.entities.BugComment;

public class BugCommentMapper {

    public static BugCommentDto toDto(BugComment comment) {
        if (comment == null) return null;
        return new BugCommentDto(
                comment.getId(),
                comment.getBody(),
                UserMapper.toDto(comment.getAuthor()),
                comment.getBug().getId());
    }

    public static BugComment toBugComment(BugCommentDto commentDto) {
        if (commentDto == null) return null;
        return BugComment.builder()
                .body(commentDto.body())
                .build();
    }
}
