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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final UserModelAssembler userAssembler;

    @GetMapping
    public CollectionModel<EntityModel<UserDto>> getAllUsers() {
        List<EntityModel<UserDto>> users = userService.findAllUsers().stream()
                .map(userAssembler::toModel)
                .toList();

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
}
