package com.projects.bugtracker.controllers;

import com.projects.bugtracker.assemblers.ProjectModelAssembler;
import com.projects.bugtracker.assemblers.UserModelAssembler;
import com.projects.bugtracker.dto.ProjectDto;
import com.projects.bugtracker.dto.UserDto;
import com.projects.bugtracker.services.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final UserModelAssembler userAssembler;
    private final ProjectModelAssembler projectAssembler;

    @GetMapping
    public CollectionModel<EntityModel<UserDto>> getAllUsers() {
        List<EntityModel<UserDto>> users = userService.findAllUsers().stream()
                .map(userAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
    }

    @PostMapping
    public void createUser(@Valid @RequestBody UserDto user) {
        userService.createUser(user);
    }

    @GetMapping("{username}")
    public EntityModel<UserDto> getUser(@PathVariable String username) {
        UserDto user = userService.findUserByUsername(username);
        return userAssembler.toModel(user);
    }

    @DeleteMapping("{username}")
    public void deleteUser(@PathVariable String username) {
        userService.deleteUserByUsername(username);
    }

    @GetMapping("{username}/owned-projects")
    public CollectionModel<EntityModel<ProjectDto>> getOwnedProjects(@PathVariable String username) {
        List<EntityModel<ProjectDto>> projects = userService.getOwnedProjects(username).stream()
                .map(projectAssembler::toModel)
                .toList();
        return CollectionModel.of(projects, linkTo(methodOn(UserController.class).getOwnedProjects(username)).withSelfRel());
    }

    @GetMapping("{username}/shared-projects")
    public CollectionModel<EntityModel<ProjectDto>> getSharedProjects(@PathVariable String username) {
        List<EntityModel<ProjectDto>> projects = userService.getSharedProjects(username).stream()
                .map(projectAssembler::toModel)
                .toList();

        return CollectionModel.of(projects, linkTo(methodOn(UserController.class).getSharedProjects(username)).withSelfRel());
    }
}
