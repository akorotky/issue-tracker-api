package com.akorotky.issuetrackerapi.dto.bug;

import com.akorotky.issuetrackerapi.dto.role.RoleDtoMapper;
import com.akorotky.issuetrackerapi.entity.Bug;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BugDtoMapper extends RoleDtoMapper {

    @Mapping(source = "bug.project.id", target = "projectId")
    BugResponseDto toDto(Bug bug);

    Bug toEntity(BugRequestDto bugRequestDto);
}
