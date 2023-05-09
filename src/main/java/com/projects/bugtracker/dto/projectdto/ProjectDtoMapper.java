package com.projects.bugtracker.dto.projectdto;

import com.projects.bugtracker.entities.Project;
import org.mapstruct.Mapper;

@Mapper
public interface ProjectDtoMapper {

    ProjectResponseDto toDto(Project project);

    Project toEntity(ProjectRequestDto projectRequestDto);
}
