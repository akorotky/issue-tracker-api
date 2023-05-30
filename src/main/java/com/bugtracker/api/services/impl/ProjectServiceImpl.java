package com.bugtracker.api.services.impl;

import com.bugtracker.api.dto.project.ProjectDtoMapper;
import com.bugtracker.api.dto.project.ProjectRequestDto;
import com.bugtracker.api.entities.Project;
import com.bugtracker.api.entities.User;
import com.bugtracker.api.exceptions.ResourceNotFoundException;
import com.bugtracker.api.security.acl.AclPermissionService;
import com.bugtracker.api.security.expressions.permissions.project.ProjectAdminPermission;
import com.bugtracker.api.security.expressions.permissions.project.ProjectReadByIdPermission;
import com.bugtracker.api.security.expressions.permissions.project.ProjectReadPermission;
import com.bugtracker.api.security.expressions.permissions.role.IsUser;
import com.bugtracker.api.services.ProjectService;
import com.bugtracker.api.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectDtoMapper projectDtoMapper;
    private final AclPermissionService aclPermissionService;
    private final Permission[] ownerPermissions = {BasePermission.READ, BasePermission.WRITE, BasePermission.DELETE, BasePermission.ADMINISTRATION};
    private final Permission[] collaboratorPermissions = {BasePermission.READ, BasePermission.CREATE};

    @IsUser
    @Override
    public Page<Project> findAllProjects(Pageable pageable) {
        Page<Project> projects = projectRepository.findAll(pageable);
        if (projects.getTotalElements() == 0) throw new ResourceNotFoundException("No projects found");
        return projects;
    }

    @IsUser
    @Override
    public Page<Project> findAllProjectsByOwner(User user, Pageable pageable) {
        Page<Project> projects = projectRepository.findByOwner(user, pageable);
        if (projects.getTotalElements() == 0)
            throw new ResourceNotFoundException("No projects with owner=" + user.getUsername() + " found");
        return projects;
    }

    @IsUser
    @Override
    public Page<Project> findAllProjectsByCollaborator(User user, Pageable pageable) {
        Page<Project> projects = projectRepository.findByCollaborator(user, pageable);
        if (projects.getTotalElements() == 0)
            throw new ResourceNotFoundException("No projects with collaborator=" + user.getUsername() + " found");
        return projects;
    }

    @IsUser
    @ProjectReadByIdPermission
    @Override
    public Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id=" + projectId + " not found"));
    }

    @IsUser
    @Override
    public Project createProject(ProjectRequestDto projectRequestDto, User user) {
        Project project = projectDtoMapper.toEntity(projectRequestDto);
        project.setOwner(user);
        projectRepository.saveAndFlush(project);
        aclPermissionService.grantPermissions(project, project.getId(), user.getUsername(), ownerPermissions);
        return project;
    }

    @ProjectAdminPermission
    @Override
    public void updateProject(Project project, ProjectRequestDto projectRequestDto) {
        project.setTitle(projectRequestDto.title());
        project.setDescription(projectRequestDto.description());
        projectRepository.save(project);
    }

    @ProjectAdminPermission
    @Override
    public void deleteProject(Project project) {
        projectRepository.deleteById(project.getId());
    }

    @ProjectReadPermission
    @Override
    public List<User> getProjectCollaborators(Project project) {
        List<User> collaborators = project.getCollaborators().stream().toList();
        if (collaborators.isEmpty())
            throw new ResourceNotFoundException("No collaborators for project with id=" + project.getId() + " found");
        return collaborators;
    }

    @ProjectAdminPermission
    @Override
    public void addProjectCollaborator(Project project, User user) {
        project.addCollaborator(user);
        projectRepository.saveAndFlush(project);
        aclPermissionService.grantPermissions(project, project.getId(), user.getUsername(), collaboratorPermissions);
    }

    @ProjectAdminPermission
    @Override
    public void removeProjectCollaborator(Project project, User user) {
        project.removeCollaborator(user);
        projectRepository.save(project);
        aclPermissionService.revokePermissions(project, project.getId(), user.getUsername(), collaboratorPermissions);
    }
}
