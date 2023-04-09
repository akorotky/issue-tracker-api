package com.projects.bugtracker.assemblers;

import com.projects.bugtracker.controllers.ProjectController;
import com.projects.bugtracker.controllers.UserController;
import com.projects.bugtracker.dto.ProjectDto;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectModelAssembler  implements RepresentationModelAssembler<ProjectDto, EntityModel<ProjectDto>> {

    @Override
    public @NonNull EntityModel<ProjectDto> toModel(ProjectDto projectDto) {
        Long projectId = projectDto.id();
        String username = projectDto.owner().username();

        return EntityModel.of(projectDto,
                linkTo(methodOn(ProjectController.class).getProject(projectId)).withSelfRel(),
                linkTo(methodOn(UserController.class).getUser(username)).withRel("owner"),
                linkTo(methodOn(ProjectController.class).getAllCollaborators(projectId)).withRel("collaborators"));
    }
}
