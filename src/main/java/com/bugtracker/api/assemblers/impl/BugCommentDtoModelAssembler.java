package com.bugtracker.api.assemblers.impl;

import com.bugtracker.api.assemblers.ModelAssembler;
import com.bugtracker.api.controllers.UserController;
import com.bugtracker.api.dto.bugcommentdto.BugCommentResponseDto;
import com.bugtracker.api.controllers.BugCommentController;
import com.bugtracker.api.controllers.BugController;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BugCommentDtoModelAssembler implements ModelAssembler<BugCommentResponseDto> {

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
