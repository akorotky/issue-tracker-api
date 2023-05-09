package com.bugtracker.api.services;

import com.bugtracker.api.entities.User;
import com.bugtracker.api.dto.projectdto.ProjectRequestDto;
import com.bugtracker.api.entities.Project;
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
