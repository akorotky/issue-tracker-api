package com.bugtracker.api.rest.impl;

import com.bugtracker.api.controller.UserController;
import com.bugtracker.api.rest.RestModelAssembler;
import com.bugtracker.api.controller.ProjectController;
import com.bugtracker.api.dto.project.ProjectResponseDto;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectDtoRestModelAssembler implements RestModelAssembler<ProjectResponseDto> {

    @Override
    public @NonNull EntityModel<ProjectResponseDto> toModel(ProjectResponseDto projectResponseDto) {
        Long projectId = projectResponseDto.id();
        String username = projectResponseDto.owner().username();

        return EntityModel.of(projectResponseDto,
                linkTo(methodOn(ProjectController.class).getProject(projectId)).withSelfRel(),
                linkTo(methodOn(UserController.class).getUser(username)).withRel("owner"),
                linkTo(methodOn(ProjectController.class).getAllCollaborators(projectId)).withRel("collaborators"),
                linkTo(methodOn(ProjectController.class).getAllBugs(projectId, Pageable.unpaged())).withRel("bugs"));
    }
}
