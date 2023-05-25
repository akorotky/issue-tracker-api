package com.bugtracker.api.dto.user;

import com.bugtracker.api.dto.role.RoleDtoMapper;
import com.bugtracker.api.entities.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserDtoMapper extends RoleDtoMapper {

    UserResponseDto toDto(User user);

    User toEntity(UserRequestDto userRequestDto);
}