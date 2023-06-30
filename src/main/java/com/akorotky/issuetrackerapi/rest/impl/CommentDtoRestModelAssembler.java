package com.akorotky.issuetrackerapi.rest.impl;

import com.akorotky.issuetrackerapi.dto.comment.CommentResponseDto;
import com.akorotky.issuetrackerapi.rest.RestModelAssembler;
import com.akorotky.issuetrackerapi.controller.UserController;
import com.akorotky.issuetrackerapi.controller.CommentController;
import com.akorotky.issuetrackerapi.controller.IssueController;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CommentDtoRestModelAssembler implements RestModelAssembler<CommentResponseDto> {

    @Override
    public @NonNull EntityModel<CommentResponseDto> toModel(CommentResponseDto commentResponseDto) {
        Long commentId = commentResponseDto.id();
        Long issueId = commentResponseDto.issueId();
        String username = commentResponseDto.author().username();

        return EntityModel.of(commentResponseDto,
                linkTo(methodOn(CommentController.class).getComment(commentId)).withSelfRel(),
                linkTo(methodOn(UserController.class).getUser(username)).withRel("author"),
                linkTo(methodOn(IssueController.class).getIssue(issueId)).withRel("issue"));
    }
}
