package com.projects.bugtracker.assemblers.impl;

import com.projects.bugtracker.assemblers.ModelAssembler;
import com.projects.bugtracker.controllers.BugController;
import com.projects.bugtracker.controllers.ProjectController;
import com.projects.bugtracker.controllers.UserController;
import com.projects.bugtracker.dto.bugdto.BugResponseDto;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
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
                linkTo(methodOn(UserController.class).getUser(username)).withRel("author"),
                linkTo(methodOn(ProjectController.class).getProject(projectId)).withRel("project"),
                linkTo(methodOn(BugController.class).getComments(bugId, Pageable.unpaged())).withRel("comments").expand());
    }
}
