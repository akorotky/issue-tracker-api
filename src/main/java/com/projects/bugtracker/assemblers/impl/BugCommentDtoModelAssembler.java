package com.projects.bugtracker.assemblers.impl;

import com.projects.bugtracker.assemblers.ModelAssembler;
import com.projects.bugtracker.controllers.BugCommentController;
import com.projects.bugtracker.controllers.BugController;
import com.projects.bugtracker.controllers.UserController;
import com.projects.bugtracker.dto.bugcommentdto.BugCommentResponseDto;
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
