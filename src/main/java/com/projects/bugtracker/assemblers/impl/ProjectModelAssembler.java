package com.projects.bugtracker.assemblers.impl;

import com.projects.bugtracker.assemblers.ModelAssembler;
import com.projects.bugtracker.controllers.ProjectController;
import com.projects.bugtracker.controllers.UserController;
import com.projects.bugtracker.dto.ProjectDto;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectModelAssembler implements ModelAssembler<ProjectDto> {

    @Override
    public @NonNull EntityModel<ProjectDto> toModel(ProjectDto projectDto) {
        Long projectId = projectDto.id();
        String username = projectDto.owner().username();

        return EntityModel.of(projectDto,
                linkTo(methodOn(ProjectController.class).getProject(projectId)).withSelfRel(),
                linkTo(methodOn(UserController.class).getUser(username)).withRel("owner"),
                linkTo(methodOn(ProjectController.class).getAllCollaborators(projectId)).withRel("collaborators"),
                linkTo(methodOn(ProjectController.class).getAllBugs(projectId, Pageable.unpaged())).withRel("bugs"));
    }
}
