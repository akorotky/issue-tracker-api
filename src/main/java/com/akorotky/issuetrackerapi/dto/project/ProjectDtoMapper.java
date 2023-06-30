package com.akorotky.issuetrackerapi.dto.project;

import com.akorotky.issuetrackerapi.dto.role.RoleDtoMapper;
import com.akorotky.issuetrackerapi.entity.Project;
import org.mapstruct.Mapper;

@Mapper
public interface ProjectDtoMapper extends RoleDtoMapper {

    ProjectResponseDto toDto(Project project);

    Project toEntity(ProjectRequestDto projectRequestDto);
}
