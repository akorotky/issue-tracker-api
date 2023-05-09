package com.projects.bugtracker.dto.bugdto;

import com.projects.bugtracker.entities.Bug;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper

public interface BugDtoMapper {
    @Mapping(source = "bug.project.id", target = "projectId")
    BugResponseDto toDto(Bug bug);

    Bug toEntity(BugRequestDto bugRequestDto);
}
