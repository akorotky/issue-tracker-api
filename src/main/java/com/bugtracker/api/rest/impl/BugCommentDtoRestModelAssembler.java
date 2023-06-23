package com.bugtracker.api.rest.impl;

import com.bugtracker.api.rest.RestModelAssembler;
import com.bugtracker.api.controller.UserController;
import com.bugtracker.api.dto.bugcomment.BugCommentResponseDto;
import com.bugtracker.api.controller.BugCommentController;
import com.bugtracker.api.controller.BugController;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BugCommentDtoRestModelAssembler implements RestModelAssembler<BugCommentResponseDto> {

    @Override
    public @NonNull EntityModel<BugCommentResponseDto> toModel(BugCommentResponseDto bugCommentResponseDto) {
        Long commentId = bugCommentResponseDto.id();
        Long bugId = bugCommentResponseDto.bugId();
        String username = bugCommentResponseDto.author().username();

        return EntityModel.of(bugCommentResponseDto,
                linkTo(methodOn(BugCommentController.class).getBugComment(commentId)).withSelfRel(),
                linkTo(methodOn(UserController.class).getUser(username)).withRel("author"),
                linkTo(methodOn(BugController.class).getBug(bugId)).withRel("bug"));
    }
}
