package com.bugtracker.api.controllers;

import com.bugtracker.api.assemblers.ModelAssembler;
import com.bugtracker.api.dto.user.UserDtoMapper;
import com.bugtracker.api.dto.user.UserRequestDto;
import com.bugtracker.api.dto.user.UserResponseDto;
import com.bugtracker.api.entities.User;
import com.bugtracker.api.services.impl.UserServiceImpl;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        userService.createUser(userRequestDto);
        URI createdUserLocation = linkTo(methodOn(UserController.class).getUser(userRequestDto.username())).toUri();
        return ResponseEntity.created(createdUserLocation).build();
    }

    @GetMapping("{username}")
    public EntityModel<UserResponseDto> getUser(@PathVariable String username) {
        UserResponseDto user = userDtoMapper.toDto(userService.findUserByUsername(username));
        return userDtoModelAssembler.toModel(user);
    }

    @PatchMapping("{username}")
    public void updateUser(@PathVariable String username, @Valid @RequestBody UserRequestDto userRequestDto) {
        User user = userService.findUserByUsername(username);
        userService.updateUser(user, userRequestDto);
    }

    @DeleteMapping("{username}")
    public void deleteUser(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        userService.deleteUser(user);
    }
}
