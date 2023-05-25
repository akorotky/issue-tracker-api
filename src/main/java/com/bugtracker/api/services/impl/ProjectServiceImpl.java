package com.bugtracker.api.services.impl;

import com.bugtracker.api.dto.project.ProjectDtoMapper;
import com.bugtracker.api.dto.project.ProjectRequestDto;
import com.bugtracker.api.entities.Project;
import com.bugtracker.api.entities.User;
import com.bugtracker.api.exceptions.ResourceNotFoundException;
import com.bugtracker.api.security.acl.AclPermissionService;
import com.bugtracker.api.security.expressions.permissions.project.ProjectAdminPermission;
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
        return projectRepository.findAll(pageable);
    }

    @IsUser
    @Override
    public Page<Project> findAllProjectsByOwner(User user, Pageable pageable) {
        return projectRepository.findByOwner(user, pageable);
    }

    @IsUser
    @Override
    public Page<Project> findAllProjectsByCollaborator(User user, Pageable pageable) {
        return projectRepository.findByCollaborator(user, pageable);
    }

    @IsUser
    @ProjectReadPermission
    @Override
    public Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
    }

    @IsUser
    @Override
    public void createProject(ProjectRequestDto projectRequestDto, User user) {
        Project project = projectDtoMapper.toEntity(projectRequestDto);
        project.setOwner(user);
        projectRepository.saveAndFlush(project);
        aclPermissionService.grantPermissions(project, project.getId(), user.getUsername(), ownerPermissions);
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

    @IsUser
    @ProjectReadPermission
    @Override
    public List<User> getProjectCollaborators(Project project) {
        return project.getCollaborators().stream().toList();
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
