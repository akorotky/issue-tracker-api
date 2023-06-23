package com.bugtracker.api.rest.impl;

import com.bugtracker.api.controller.UserController;
import com.bugtracker.api.rest.RestModelAssembler;
import com.bugtracker.api.controller.BugController;
import com.bugtracker.api.controller.ProjectController;
import com.bugtracker.api.dto.bug.BugResponseDto;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BugDtoRestModelAssembler implements RestModelAssembler<BugResponseDto> {

    @Override
    public @NonNull EntityModel<BugResponseDto> toModel(BugResponseDto bugResponseDto) {
        Long bugId = bugResponseDto.id();
        String username = bugResponseDto.author().username();
        Long projectId = bugResponseDto.projectId();

        return EntityModel.of(bugResponseDto,
                linkTo(methodOn(BugController.class).getBug(bugId)).withSelfRel(),
                linkTo(methodOn(UserController.class).getUser(username)).withRel("author"),
                linkTo(methodOn(ProjectController.class).getProject(projectId)).withRel("project"),
                linkTo(methodOn(BugController.class).getComments(bugId, Pageable.unpaged())).withRel("comments").expand());
    }
}
