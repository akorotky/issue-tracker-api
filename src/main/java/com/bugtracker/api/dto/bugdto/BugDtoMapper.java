package com.bugtracker.api.dto.bugdto;

import com.bugtracker.api.entities.Bug;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper

public interface BugDtoMapper {
    @Mapping(source = "bug.project.id", target = "projectId")
    BugResponseDto toDto(Bug bug);

    Bug toEntity(BugRequestDto bugRequestDto);
}
