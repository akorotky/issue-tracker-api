package com.projects.bugtracker.dto;

import com.projects.bugtracker.entities.Bug;

public class BugMapper {

    public static BugDto toDto(Bug bug) {
        if (bug == null) return null;
        return new BugDto(
                bug.getId(),
                bug.getTitle(),
                bug.getDescription(),
                UserMapper.toDto(bug.getAuthor()));
    }

    public static Bug toBug(BugDto bugDto) {
        if (bugDto == null) return null;
        return Bug.builder()
                .title(bugDto.title())
                .description(bugDto.description())
                .build();
    }
}
