package com.akorotky.issuetrackerapi.dto.user;

import com.akorotky.issuetrackerapi.dto.role.RoleDtoMapper;
import com.akorotky.issuetrackerapi.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserDtoMapper extends RoleDtoMapper {

    UserResponseDto toDto(User user);

    User toEntity(UserRequestDto userRequestDto);
}