package com.projects.bugtracker.services;

import com.projects.bugtracker.dto.BugDto;
import com.projects.bugtracker.dto.ProjectDto;
import com.projects.bugtracker.dto.UserDto;
import com.projects.bugtracker.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {

    ProjectDto findProjectById(Long id);

    Page<ProjectDto> findAllProjects(Pageable pageable);

    Page<ProjectDto> findAllProjectsByOwner(String username, Pageable pageable);

    Page<ProjectDto> findAllProjectsByCollaborator(String username, Pageable pageable);

    Page<BugDto> findAllBugsByProjectId(Long projectId, Pageable pageable);

    void createProject(ProjectDto projectDto, User user);

    void updateProject(ProjectDto projectDto, Long projectId);

    void deleteProjectById(Long id);

    List<UserDto> getProjectCollaborators(Long projectId);

    void addProjectCollaborator(Long projectId, String username);

    void removeProjectCollaborator(Long projectId, String username);
}
