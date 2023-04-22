package com.projects.bugtracker.controllers;

import com.projects.bugtracker.assemblers.UserModelAssembler;
import com.projects.bugtracker.dto.UserDto;
import com.projects.bugtracker.services.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final UserModelAssembler userModelAssembler;
    private final PagedResourcesAssembler<UserDto> pagedResourcesAssembler;

    @GetMapping
    public PagedModel<EntityModel<UserDto>> getUsersPage(
            @PageableDefault(page = 0, size = 15)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "username", direction = Sort.Direction.DESC)
            }) Pageable pageable) {
        Page<UserDto> usersPage = userService.findAllUsers(pageable);

        return pagedResourcesAssembler.toModel(usersPage, userModelAssembler);
    }

    @PostMapping
    public void createUser(@Valid @RequestBody UserDto user) {
        userService.createUser(user);
    }

    @GetMapping("{username}")
    public EntityModel<UserDto> getUser(@PathVariable String username) {
        UserDto user = userService.findUserByUsername(username);
        return userModelAssembler.toModel(user);
    }

    @DeleteMapping("{username}")
    public void deleteUser(@PathVariable String username) {
        userService.deleteUserByUsername(username);
    }
}
