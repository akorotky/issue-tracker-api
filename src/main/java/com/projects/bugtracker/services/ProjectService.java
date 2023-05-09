package com.projects.bugtracker.services;

import com.projects.bugtracker.dto.projectdto.ProjectRequestDto;
import com.projects.bugtracker.entities.Project;
import com.projects.bugtracker.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {

    Project findProjectById(Long id);

    Page<Project> findAllProjects(Pageable pageable);

    Page<Project> findAllProjectsByOwner(String username, Pageable pageable);

    Page<Project> findAllProjectsByCollaborator(String username, Pageable pageable);

    void createProject(ProjectRequestDto projectRequestDto, User user);

    void updateProject(ProjectRequestDto projectRequestDto, Long projectId);

    void deleteProjectById(Long id);

    List<User> getProjectCollaborators(Long projectId);

    void addProjectCollaborator(Long projectId, String username);

    void removeProjectCollaborator(Long projectId, String username);
}
