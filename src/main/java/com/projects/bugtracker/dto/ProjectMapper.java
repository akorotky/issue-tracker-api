package com.projects.bugtracker.dto;

import com.projects.bugtracker.entities.Project;

public class ProjectMapper {

    public static ProjectDto toDto(Project project) {
        if (project == null) return null;
        return new ProjectDto(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                UserMapper.toDto(project.getOwner())
        );
    }

    public static Project toProject(ProjectDto projectDto) {
        if (projectDto == null) return null;
        return Project.builder()
                .title(projectDto.title())
                .description(projectDto.description())
                .build();
    }
}
