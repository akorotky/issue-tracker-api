package com.bugtracker.api.services;

import com.bugtracker.api.entities.User;
import com.bugtracker.api.dto.project.ProjectRequestDto;
import com.bugtracker.api.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {

    Project findProjectById(Long projectId);

    Page<Project> findAllProjects(Pageable pageable);

    Page<Project> findAllProjectsByOwner(User user, Pageable pageable);

    Page<Project> findAllProjectsByCollaborator(User user, Pageable pageable);

    void createProject(ProjectRequestDto projectRequestDto, User user);

    void updateProject(Project project, ProjectRequestDto projectRequestDto);

    void deleteProject(Project project);

    List<User> getProjectCollaborators(Project project);

    void addProjectCollaborator(Project project, User user);

    void removeProjectCollaborator(Project project, User user);
}
