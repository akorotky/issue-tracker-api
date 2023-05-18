package com.bugtracker.api.services.impl;

import com.bugtracker.api.dto.projectdto.ProjectDtoMapper;
import com.bugtracker.api.dto.projectdto.ProjectRequestDto;
import com.bugtracker.api.entities.Project;
import com.bugtracker.api.entities.User;
import com.bugtracker.api.exceptions.ResourceNotFoundException;
import com.bugtracker.api.repositories.UserRepository;
import com.bugtracker.api.security.acl.AclPermissionServiceImpl;
import com.bugtracker.api.services.ProjectService;
import com.bugtracker.api.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectDtoMapper projectDtoMapper;
    private final AclPermissionServiceImpl aclPermissionServiceImpl;

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
        projectRepository.saveAndFlush(project);
        // grant project owner admin permissions
        aclPermissionServiceImpl.grantPermissions(project, project.getId(), user.getUsername(), BasePermission.ADMINISTRATION);
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
