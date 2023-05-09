package com.projects.bugtracker.services.impl;

import com.projects.bugtracker.dto.projectdto.ProjectDtoMapper;
import com.projects.bugtracker.dto.projectdto.ProjectRequestDto;
import com.projects.bugtracker.exceptions.ResourceNotFoundException;
import com.projects.bugtracker.entities.Project;
import com.projects.bugtracker.entities.User;
import com.projects.bugtracker.repositories.ProjectRepository;
import com.projects.bugtracker.repositories.UserRepository;
import com.projects.bugtracker.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectDtoMapper projectDtoMapper;

    @Override
    public Page<Project> findAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Override
    public Page<Project> findAllProjectsByOwner(String username, Pageable pageable) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return projectRepository.findByOwner_Username(username, pageable);
    }

    @Override
    public Page<Project> findAllProjectsByCollaborator(String username, Pageable pageable) {
        User collaborator = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return projectRepository.findByCollaborators_Username(username, pageable);
    }

    @Override
    public Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
    }

    @Override
    public void createProject(ProjectRequestDto projectRequestDto, User user) {
        Project project = projectDtoMapper.toEntity(projectRequestDto);
        project.setOwner(user);
        projectRepository.save(project);
    }

    @Override
    public void updateProject(ProjectRequestDto projectRequestDto, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        project.setTitle(projectRequestDto.title());
        project.setTitle(projectRequestDto.description());

        projectRepository.save(project);
    }

    @Override
    public void deleteProjectById(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    @Override
    public List<User> getProjectCollaborators(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
        return project.getCollaborators().stream().toList();
    }

    @Override
    public void addProjectCollaborator(Long projectId, String username) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        project.addCollaborator(user);
        projectRepository.save(project);
    }

    @Override
    public void removeProjectCollaborator(Long projectId, String username) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", username));
        project.removeCollaborator(user);
        projectRepository.save(project);
    }

}
