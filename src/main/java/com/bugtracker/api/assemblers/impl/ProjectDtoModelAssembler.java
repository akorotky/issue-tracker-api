package com.bugtracker.api.assemblers.impl;

import com.bugtracker.api.controllers.UserController;
import com.bugtracker.api.assemblers.ModelAssembler;
import com.bugtracker.api.controllers.ProjectController;
import com.bugtracker.api.dto.projectdto.ProjectResponseDto;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectDtoModelAssembler implements ModelAssembler<ProjectResponseDto> {

    @Override
    public @NonNull EntityModel<ProjectResponseDto> toModel(ProjectResponseDto projectResponseDto) {
        Long projectId = projectResponseDto.id();
        String username = projectResponseDto.owner().username();

        return EntityModel.of(projectResponseDto,
                linkTo(methodOn(ProjectController.class).getProject(projectId)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUser(username)).withRel("owner"),
                linkTo(methodOn(ProjectController.class).getAllCollaborators(projectId)).withRel("collaborators"),
                linkTo(methodOn(ProjectController.class).getAllBugs(projectId, Pageable.unpaged())).withRel("bugs"));
    }
}
