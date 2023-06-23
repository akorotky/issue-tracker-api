package com.bugtracker.api.dto.bug;

import com.bugtracker.api.dto.role.RoleDtoMapper;
import com.bugtracker.api.entity.Bug;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BugDtoMapper extends RoleDtoMapper {

    @Mapping(source = "bug.project.id", target = "projectId")
    BugResponseDto toDto(Bug bug);

    Bug toEntity(BugRequestDto bugRequestDto);
}
