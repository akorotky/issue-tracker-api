package com.bugtracker.api.rest.impl;

import com.bugtracker.api.controller.ProjectController;
import com.bugtracker.api.controller.UserController;
import com.bugtracker.api.rest.RestModelAssembler;
import com.bugtracker.api.dto.user.UserResponseDto;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDtoRestModelAssembler implements RestModelAssembler<UserResponseDto> {

    @Override
    public @NonNull EntityModel<UserResponseDto> toModel(UserResponseDto userResponseDto) {
        String username = userResponseDto.username();

        return EntityModel.of(userResponseDto,
                linkTo(methodOn(UserController.class).getUser(username)).withSelfRel(),
                linkTo(methodOn(ProjectController.class).getProjectsPage(username, null, Pageable.unpaged())).withRel("owned-projects").expand(),
                linkTo(methodOn(ProjectController.class).getProjectsPage(null, username, Pageable.unpaged())).withRel("shared-projects").expand());
    }
}
