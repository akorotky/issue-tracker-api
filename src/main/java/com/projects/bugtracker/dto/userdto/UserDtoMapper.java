package com.projects.bugtracker.dto.userdto;

import com.projects.bugtracker.entities.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserDtoMapper {

    UserResponseDto toDto(User user);

    User toEntity(UserRequestDto userRequestDto);
}