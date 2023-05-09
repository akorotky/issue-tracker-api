package com.bugtracker.api.dto.projectdto;

import com.bugtracker.api.entities.Project;
import org.mapstruct.Mapper;

@Mapper
public interface ProjectDtoMapper {

    ProjectResponseDto toDto(Project project);

    Project toEntity(ProjectRequestDto projectRequestDto);
}
