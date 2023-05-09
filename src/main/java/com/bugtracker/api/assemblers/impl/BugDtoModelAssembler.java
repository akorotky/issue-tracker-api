package com.bugtracker.api.assemblers.impl;

import com.bugtracker.api.controllers.UserController;
import com.bugtracker.api.assemblers.ModelAssembler;
import com.bugtracker.api.controllers.BugController;
import com.bugtracker.api.controllers.ProjectController;
import com.bugtracker.api.dto.bugdto.BugResponseDto;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BugDtoModelAssembler implements ModelAssembler<BugResponseDto> {

    @Override
    public @NonNull EntityModel<BugResponseDto> toModel(BugResponseDto bugResponseDto) {
        Long bugId = bugResponseDto.id();
        String username = bugResponseDto.author().username();
        Long projectId = bugResponseDto.projectId();

        return EntityModel.of(bugResponseDto,
                linkTo(methodOn(BugController.class).getBug(bugId)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUser(username)).withRel("author"),
                linkTo(methodOn(ProjectController.class).getProject(projectId)).withRel("project"),
                linkTo(methodOn(BugController.class).getComments(bugId, Pageable.unpaged())).withRel("comments").expand());
    }
}
