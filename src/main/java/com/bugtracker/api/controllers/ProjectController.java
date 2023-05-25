package com.bugtracker.api.controllers;

import com.bugtracker.api.dto.bug.BugResponseDto;
import com.bugtracker.api.dto.project.ProjectRequestDto;
import com.bugtracker.api.dto.project.ProjectResponseDto;
import com.bugtracker.api.entities.Project;
import com.bugtracker.api.entities.User;
import com.bugtracker.api.security.principal.CurrentUser;
import com.bugtracker.api.services.BugService;
import com.bugtracker.api.services.ProjectService;
import com.bugtracker.api.assemblers.ModelAssembler;
import com.bugtracker.api.dto.bug.BugDtoMapper;
import com.bugtracker.api.dto.project.ProjectDtoMapper;
import com.bugtracker.api.dto.user.UserDtoMapper;
import com.bugtracker.api.dto.user.UserResponseDto;
import com.bugtracker.api.security.principal.UserPrincipal;
import com.bugtracker.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private final BugService bugService;
    private final BugDtoMapper bugDtoMapper;
    private final UserDtoMapper userDtoMapper;
    private final ProjectDtoMapper projectDtoMapper;
    private final ModelAssembler<ProjectResponseDto> projectDtoModelAssembler;
    private final ModelAssembler<UserResponseDto> userDtoModelAssembler;
    private final ModelAssembler<BugResponseDto> bugDtoModelAssembler;
    private final PagedResourcesAssembler<ProjectResponseDto> projectDtoPagedResourcesAssembler;
    private final PagedResourcesAssembler<BugResponseDto> bugDtoPagedResourcesAssembler;

    @GetMapping
    public PagedModel<EntityModel<ProjectResponseDto>> getProjectsPage(
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) String collaborator,
            @PageableDefault(page = 0, size = 20) Pageable pageable) {
        Page<Project> projectsPage;
        User user;

        if (owner == null && collaborator == null)
            projectsPage = projectService.findAllProjects(pageable);
        else if (owner != null) {
            user = userService.findUserByUsername(owner);
            projectsPage = projectService.findAllProjectsByOwner(user, pageable);
        } else {
            user = userService.findUserByUsername(collaborator);
            projectsPage = projectService.findAllProjectsByCollaborator(user, pageable);
        }

        return projectDtoPagedResourcesAssembler.toModel(projectsPage.map(projectDtoMapper::toDto), projectDtoModelAssembler);
    }

    @GetMapping("{projectId}")
    public EntityModel<ProjectResponseDto> getProject(@PathVariable Long projectId) {
        ProjectResponseDto project = projectDtoMapper.toDto(projectService.findProjectById(projectId));
        return projectDtoModelAssembler.toModel(project);
    }

    @PatchMapping("{projectId}")
    public void updateProject(@RequestBody ProjectRequestDto projectRequestDto, @PathVariable Long projectId) {
        Project project = projectService.findProjectById(projectId);
        projectService.updateProject(project, projectRequestDto);
    }

    @DeleteMapping("{projectId}")
    public void deleteProject(@PathVariable Long projectId) {
        Project project = projectService.findProjectById(projectId);
        projectService.deleteProject(project);
    }

    @PostMapping
    public void createProject(@RequestBody ProjectRequestDto project, @CurrentUser UserPrincipal currentUser) {
        projectService.createProject(project, currentUser.user());
    }

    @GetMapping("{projectId}/collaborators")
    public CollectionModel<EntityModel<UserResponseDto>> getAllCollaborators(@PathVariable Long projectId) {
        Project project = projectService.findProjectById(projectId);
        List<EntityModel<UserResponseDto>> collaborators = projectService.getProjectCollaborators(project).stream()
                .map(userDtoMapper::toDto)
                .map(userDtoModelAssembler::toModel)
                .toList();

        return CollectionModel.of(collaborators, linkTo(methodOn(ProjectController.class).getAllCollaborators(projectId)).withSelfRel());
    }

    @PutMapping("{projectId}/collaborators/{username}")
    public void addCollaborator(@PathVariable Long projectId, @PathVariable String username) {
        Project project = projectService.findProjectById(projectId);
        User collaborator = userService.findUserByUsername(username);
        projectService.addProjectCollaborator(project, collaborator);
    }

    @DeleteMapping("{projectId}/collaborators/{username}")
    public void removeCollaborator(@PathVariable Long projectId, @PathVariable String username) {
        Project project = projectService.findProjectById(projectId);
        User collaborator = userService.findUserByUsername(username);
        projectService.removeProjectCollaborator(project, collaborator);
    }

    @GetMapping("{projectId}/bugs")
    public CollectionModel<EntityModel<BugResponseDto>> getAllBugs(
            @PathVariable Long projectId,
            @PageableDefault(page = 0, size = 15) Pageable pageable) {
        Project project = projectService.findProjectById(projectId);
        Page<BugResponseDto> bugsPage = bugService.findAllBugsByProject(project, pageable).map(bugDtoMapper::toDto);
        return bugDtoPagedResourcesAssembler.toModel(bugsPage, bugDtoModelAssembler);
    }
}
