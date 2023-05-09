package com.bugtracker.api.dto.userdto;

import com.bugtracker.api.entities.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserDtoMapper {

    UserResponseDto toDto(User user);

    User toEntity(UserRequestDto userRequestDto);
}