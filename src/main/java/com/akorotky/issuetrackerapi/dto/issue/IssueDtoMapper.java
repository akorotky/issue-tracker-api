package com.akorotky.issuetrackerapi.dto.issue;

import com.akorotky.issuetrackerapi.dto.role.RoleDtoMapper;
import com.akorotky.issuetrackerapi.entity.Issue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface IssueDtoMapper extends RoleDtoMapper {

    @Mapping(source = "issue.project.id", target = "projectId")
    IssueResponseDto toDto(Issue issue);

    Issue toEntity(IssueRequestDto issueRequestDto);
}
