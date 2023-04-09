package com.projects.bugtracker.dto;

import com.projects.bugtracker.entities.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) return null;
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRoles().stream().map(RoleMapper::toDto).collect(Collectors.toSet()));
    }

    public static User toUser(UserDto userDto) {
        if (userDto == null) return null;
        return User.builder()
                .username(userDto.username())
                .password(userDto.password())
                .email(userDto.email())
                .roles(userDto.roles().stream().map(RoleMapper::toRole).collect(Collectors.toSet()))
                .build();
    }
}
