package com.projects.bugtracker.services;

import com.projects.bugtracker.dto.BugDto;
import com.projects.bugtracker.dto.ProjectDto;
import com.projects.bugtracker.dto.UserDto;
import com.projects.bugtracker.entities.User;

import java.util.List;

public interface ProjectService {

    ProjectDto findProjectById(Long id);

    List<ProjectDto> findAllProjects();

    List<ProjectDto> findAllProjectsByOwner(String username);

    List<ProjectDto> findAllProjectsByCollaborator(String username);

    List<BugDto> findAllBugs(Long projectId);

    void createProject(ProjectDto projectDto, User user);

    void updateProject(ProjectDto projectDto, Long projectId);

    void deleteProjectById(Long id);

    List<UserDto> getProjectCollaborators(Long projectId);

    void addProjectCollaborator(Long projectId, String username);

    void removeProjectCollaborator(Long projectId, String username);

}
