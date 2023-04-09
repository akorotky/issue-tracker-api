package com.projects.bugtracker.assemblers;

import com.projects.bugtracker.controllers.UserController;
import com.projects.bugtracker.dto.UserDto;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {

    @Override
    public @NonNull EntityModel<UserDto> toModel(UserDto userDto) {
        String username = userDto.username();

        return EntityModel.of(userDto,
                linkTo(methodOn(UserController.class).getUser(username)).withSelfRel(),
                linkTo(methodOn(UserController.class).getOwnedProjects(username)).withRel("owned-projects"),
                linkTo(methodOn(UserController.class).getSharedProjects(username)).withRel("shared-projects"));
    }
}
