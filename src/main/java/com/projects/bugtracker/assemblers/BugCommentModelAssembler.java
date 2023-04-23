package com.projects.bugtracker.assemblers;

import com.projects.bugtracker.controllers.BugCommentController;
import com.projects.bugtracker.controllers.BugController;
import com.projects.bugtracker.controllers.UserController;
import com.projects.bugtracker.dto.BugCommentDto;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BugCommentModelAssembler implements RepresentationModelAssembler<BugCommentDto, EntityModel<BugCommentDto>> {

    @Override
    public @NonNull EntityModel<BugCommentDto> toModel(BugCommentDto bugCommentDto) {
        Long commentId = bugCommentDto.id();
        Long bugId = bugCommentDto.bugId();
        String username = bugCommentDto.author().username();

        return EntityModel.of(bugCommentDto,
                linkTo(methodOn(BugCommentController.class).getBugComment(commentId)).withSelfRel(),
                linkTo(methodOn(UserController.class).getUser(username)).withRel("author"),
                linkTo(methodOn(BugController.class).getBug(bugId)).withRel("bug"));
    }
}
