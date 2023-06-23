package com.bugtracker.api.controller;

import com.bugtracker.api.rest.RestModelAssembler;
import com.bugtracker.api.dto.user.UserDtoMapper;
import com.bugtracker.api.dto.user.UserRequestDto;
import com.bugtracker.api.dto.user.UserResponseDto;
import com.bugtracker.api.entity.User;
import com.bugtracker.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RestModelAssembler<UserResponseDto> userDtoRestModelAssembler;
    private final PagedResourcesAssembler<UserResponseDto> userDtoPagedResourcesAssembler;
    private final UserDtoMapper userDtoMapper;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<PagedModel<EntityModel<UserResponseDto>>> getUsersPage(
            @PageableDefault(page = 0, size = 15)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "username", direction = Sort.Direction.DESC)
            }) Pageable pageable) {
        Page<UserResponseDto> userDtosPage = userService.findAllUsers(pageable).map(userDtoMapper::toDto);
        PagedModel<EntityModel<UserResponseDto>> userDtosPagedModel = userDtoPagedResourcesAssembler.toModel(userDtosPage, userDtoRestModelAssembler);
        return ResponseEntity.ok(userDtosPagedModel);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        userService.createUser(userRequestDto);
        URI createdUserUri = linkTo(methodOn(UserController.class).getUser(userRequestDto.username())).toUri();
        return ResponseEntity.created(createdUserUri).build();
    }

    @GetMapping(path = "{username}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<UserResponseDto>> getUser(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        UserResponseDto userDto = userDtoMapper.toDto(user);
        EntityModel<UserResponseDto> userDtoModel = userDtoRestModelAssembler.toModel(userDto);
        return ResponseEntity.ok(userDtoModel);
    }

    @PatchMapping(path = "{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUser(@PathVariable String username, @Valid @RequestBody UserRequestDto userRequestDto) {
        User user = userService.findUserByUsername(username);
        userService.updateUser(user, userRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        userService.deleteUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
