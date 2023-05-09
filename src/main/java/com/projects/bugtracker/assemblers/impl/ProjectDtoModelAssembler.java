package com.projects.bugtracker.assemblers.impl;

import com.projects.bugtracker.assemblers.ModelAssembler;
import com.projects.bugtracker.controllers.ProjectController;
import com.projects.bugtracker.controllers.UserController;
import com.projects.bugtracker.dto.projectdto.ProjectResponseDto;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
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
                linkTo(methodOn(UserController.class).getUser(username)).withRel("owner"),
                linkTo(methodOn(ProjectController.class).getAllCollaborators(projectId)).withRel("collaborators"),
                linkTo(methodOn(ProjectController.class).getAllBugs(projectId, Pageable.unpaged())).withRel("bugs"));
    }
}
