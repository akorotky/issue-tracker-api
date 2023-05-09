package com.projects.bugtracker.assemblers.impl;

import com.projects.bugtracker.assemblers.ModelAssembler;
import com.projects.bugtracker.controllers.ProjectController;
import com.projects.bugtracker.controllers.UserController;
import com.projects.bugtracker.dto.UserDto;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements ModelAssembler<UserDto> {

    @Override
    public @NonNull EntityModel<UserDto> toModel(UserDto userDto) {
        String username = userDto.username();

        return EntityModel.of(userDto,
                linkTo(methodOn(UserController.class).getUser(username)).withSelfRel(),
                linkTo(methodOn(ProjectController.class).getProjectsPage(username, null, Pageable.unpaged())).withRel("owned-projects").expand(),
                linkTo(methodOn(ProjectController.class).getProjectsPage(null, username, Pageable.unpaged())).withRel("shared-projects").expand());
    }
}
