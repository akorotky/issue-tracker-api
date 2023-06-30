package com.akorotky.issuetrackerapi.rest.impl;

import com.akorotky.issuetrackerapi.dto.project.ProjectResponseDto;
import com.akorotky.issuetrackerapi.rest.RestModelAssembler;
import com.akorotky.issuetrackerapi.controller.UserController;
import com.akorotky.issuetrackerapi.controller.ProjectController;
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
                linkTo(methodOn(ProjectController.class).getAllIssues(projectId, Pageable.unpaged())).withRel("issues"));
    }
}
