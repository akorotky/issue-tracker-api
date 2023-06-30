package com.akorotky.issuetrackerapi.service;

import com.akorotky.issuetrackerapi.dto.project.ProjectDtoMapper;
import com.akorotky.issuetrackerapi.dto.project.ProjectRequestDto;
import com.akorotky.issuetrackerapi.entity.Project;
import com.akorotky.issuetrackerapi.entity.User;
import com.akorotky.issuetrackerapi.exception.ResourceNotFoundException;
import com.akorotky.issuetrackerapi.security.acl.AclPermissionService;
import com.akorotky.issuetrackerapi.security.expressions.permissions.project.ProjectAdminPermission;
import com.akorotky.issuetrackerapi.security.expressions.permissions.project.ProjectReadByIdPermission;
import com.akorotky.issuetrackerapi.security.expressions.permissions.project.ProjectReadPermission;
import com.akorotky.issuetrackerapi.security.expressions.permissions.role.IsUser;
import com.akorotky.issuetrackerapi.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectDtoMapper projectDtoMapper;
    private final AclPermissionService aclPermissionService;
    private final Permission[] ownerPermissions = {BasePermission.READ, BasePermission.WRITE, BasePermission.DELETE, BasePermission.ADMINISTRATION};
    private final Permission[] collaboratorPermissions = {BasePermission.READ, BasePermission.CREATE};

    @IsUser
    @Transactional(readOnly = true)
    public Page<Project> findAllProjects(Pageable pageable) {
        Page<Project> projects = projectRepository.findAll(pageable);
        if (projects.isEmpty()) throw new ResourceNotFoundException("No projects found");
        return projects;
    }

    @IsUser
    @Transactional(readOnly = true)
    public Page<Project> findAllProjectsByOwner(User user, Pageable pageable) {
        Page<Project> projects = projectRepository.findByOwner(user, pageable);
        if (projects.isEmpty())
            throw new ResourceNotFoundException("No projects with owner=" + user.getUsername() + " found");
        return projects;
    }

    @IsUser
    @Transactional(readOnly = true)
    public Page<Project> findAllProjectsByCollaborator(User user, Pageable pageable) {
        Page<Project> projects = projectRepository.findByCollaborator(user, pageable);
        if (projects.isEmpty())
            throw new ResourceNotFoundException("No projects with collaborator=" + user.getUsername() + " found");
        return projects;
    }

    @IsUser
    @ProjectReadByIdPermission
    @Transactional(readOnly = true)
    public Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id=" + projectId + " not found"));
    }

    @IsUser
    public Project createProject(ProjectRequestDto projectRequestDto, User user) {
        Project project = projectDtoMapper.toEntity(projectRequestDto);
        project.setOwner(user);
        projectRepository.saveAndFlush(project);
        aclPermissionService.grantPermissions(project, project.getId(), user.getUsername(), ownerPermissions);
        return project;
    }

    @ProjectAdminPermission
    public void updateProject(Project project, ProjectRequestDto projectRequestDto) {
        project.setTitle(projectRequestDto.title());
        project.setDescription(projectRequestDto.description());
        projectRepository.save(project);
    }

    @ProjectAdminPermission
    public void deleteProject(Project project) {
        projectRepository.deleteById(project.getId());
    }

    @ProjectReadPermission
    public List<User> getProjectCollaborators(Project project) {
        List<User> collaborators = project.getCollaboratorsView().stream().toList();
        if (collaborators.isEmpty())
            throw new ResourceNotFoundException("No collaborators for project with id=" + project.getId() + " found");
        return collaborators;
    }

    @ProjectAdminPermission
    public void addProjectCollaborator(Project project, User user) {
        project.addCollaborator(user);
        projectRepository.saveAndFlush(project);
        aclPermissionService.grantPermissions(project, project.getId(), user.getUsername(), collaboratorPermissions);
    }

    @ProjectAdminPermission
    public void removeProjectCollaborator(Project project, User user) {
        project.removeCollaborator(user);
        projectRepository.save(project);
        aclPermissionService.revokePermissions(project, project.getId(), user.getUsername(), collaboratorPermissions);
    }
}
