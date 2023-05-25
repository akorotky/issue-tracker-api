package com.bugtracker.api.dto.bugcomment;

import com.bugtracker.api.dto.role.RoleDtoMapper;
import com.bugtracker.api.entities.BugComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BugCommentDtoMapper extends RoleDtoMapper {

    @Mapping(source = "bugComment.bug.id", target = "bugId")
    BugCommentResponseDto toDto(BugComment bugComment);

    BugComment toEntity(BugCommentRequestDto bugCommentRequestDto);
}
