package com.bugtracker.api.dto.projectdto;

import com.bugtracker.api.dto.roledto.RoleDtoMapper;
import com.bugtracker.api.entities.Project;
import org.mapstruct.Mapper;

@Mapper
public interface ProjectDtoMapper extends RoleDtoMapper {

    ProjectResponseDto toDto(Project project);

    Project toEntity(ProjectRequestDto projectRequestDto);
}
