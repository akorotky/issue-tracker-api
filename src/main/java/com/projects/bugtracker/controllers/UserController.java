package com.projects.bugtracker.controllers;

import com.projects.bugtracker.assemblers.ModelAssembler;
import com.projects.bugtracker.dto.userdto.UserDtoMapper;
import com.projects.bugtracker.dto.userdto.UserRequestDto;
import com.projects.bugtracker.dto.userdto.UserResponseDto;
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
    private final ModelAssembler<UserResponseDto> userDtoModelAssembler;
    private final PagedResourcesAssembler<UserResponseDto> userDtoPagedResourcesAssembler;
    private final UserDtoMapper userDtoMapper;

    @GetMapping
    public PagedModel<EntityModel<UserResponseDto>> getUsersPage(
            @PageableDefault(page = 0, size = 15)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "username", direction = Sort.Direction.DESC)
            }) Pageable pageable) {
        Page<UserResponseDto> usersPage = userService.findAllUsers(pageable).map(userDtoMapper::toDto);

        return userDtoPagedResourcesAssembler.toModel(usersPage, userDtoModelAssembler);
    }

    @PostMapping
    public void createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        userService.createUser(userRequestDto);
    }

    @GetMapping("{username}")
    public EntityModel<UserResponseDto> getUser(@PathVariable String username) {
        UserResponseDto user = userDtoMapper.toDto(userService.findUserByUsername(username));
        return userDtoModelAssembler.toModel(user);
    }

    @DeleteMapping("{username}")
    public void deleteUser(@PathVariable String username) {
        userService.deleteUserByUsername(username);
    }
}
