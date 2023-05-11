package com.bugtracker.api.dto.bugdto;

import com.bugtracker.api.dto.roledto.RoleDtoMapper;
import com.bugtracker.api.entities.Bug;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BugDtoMapper extends RoleDtoMapper {

    @Mapping(source = "bug.project.id", target = "projectId")
    BugResponseDto toDto(Bug bug);

    Bug toEntity(BugRequestDto bugRequestDto);
}
