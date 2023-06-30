package com.akorotky.issuetrackerapi.dto.bugcomment;

import com.akorotky.issuetrackerapi.dto.role.RoleDtoMapper;
import com.akorotky.issuetrackerapi.entity.BugComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BugCommentDtoMapper extends RoleDtoMapper {

    @Mapping(source = "bugComment.bug.id", target = "bugId")
    BugCommentResponseDto toDto(BugComment bugComment);

    BugComment toEntity(BugCommentRequestDto bugCommentRequestDto);
}
