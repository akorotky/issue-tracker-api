package com.akorotky.issuetrackerapi.dto.comment;

import com.akorotky.issuetrackerapi.dto.role.RoleDtoMapper;
import com.akorotky.issuetrackerapi.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommentDtoMapper extends RoleDtoMapper {

    @Mapping(source = "comment.issue.id", target = "issueId")
    CommentResponseDto toDto(Comment comment);

    Comment toEntity(CommentRequestDto commentRequestDto);
}
