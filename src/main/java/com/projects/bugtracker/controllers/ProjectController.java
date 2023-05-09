package com.projects.bugtracker.controllers;

import com.projects.bugtracker.assemblers.ModelAssembler;
import com.projects.bugtracker.dto.bugdto.BugDtoMapper;
import com.projects.bugtracker.dto.bugdto.BugResponseDto;
import com.projects.bugtracker.dto.projectdto.ProjectDtoMapper;
import com.projects.bugtracker.dto.projectdto.ProjectRequestDto;
import com.projects.bugtracker.dto.projectdto.ProjectResponseDto;
import com.projects.bugtracker.dto.userdto.UserDtoMapper;
import com.projects.bugtracker.dto.userdto.UserResponseDto;
import com.projects.bugtracker.security.principal.CurrentUser;
import com.projects.bugtracker.security.principal.UserPrincipal;
import com.projects.bugtracker.services.BugService;
import com.projects.bugtracker.services.ProjectService;
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
        Page<ProjectResponseDto> projectsPage;

        if (owner == null && collaborator == null)
            projectsPage = projectService.findAllProjects(pageable).map(projectDtoMapper::toDto);
        else if (owner != null)
            projectsPage = projectService.findAllProjectsByOwner(owner, pageable).map(projectDtoMapper::toDto);
        else
            projectsPage = projectService.findAllProjectsByCollaborator(collaborator, pageable).map(projectDtoMapper::toDto);

        return projectDtoPagedResourcesAssembler.toModel(projectsPage, projectDtoModelAssembler);
    }

    @GetMapping("{projectId}")
    public EntityModel<ProjectResponseDto> getProject(@PathVariable Long projectId) {
        ProjectResponseDto project = projectDtoMapper.toDto(projectService.findProjectById(projectId));
        return projectDtoModelAssembler.toModel(project);
    }

    @PatchMapping("{projectId}")
    public void updateProject(@RequestBody ProjectRequestDto project, @PathVariable Long projectId) {
        projectService.updateProject(project, projectId);
    }

    @DeleteMapping("{projectId}")
    public void deleteProject(@PathVariable Long projectId) {
        projectService.deleteProjectById(projectId);
    }

    @PostMapping
    public void createProject(@RequestBody ProjectRequestDto project, @CurrentUser UserPrincipal currentUser) {
        projectService.createProject(project, currentUser.getUser());
    }

    @GetMapping("{projectId}/collaborators")
    public CollectionModel<EntityModel<UserResponseDto>> getAllCollaborators(@PathVariable Long projectId) {
        List<EntityModel<UserResponseDto>> collaborators = projectService.getProjectCollaborators(projectId).stream()
                .map(userDtoMapper::toDto)
                .map(userDtoModelAssembler::toModel)
                .toList();

        return CollectionModel.of(collaborators, linkTo(methodOn(ProjectController.class).getAllCollaborators(projectId)).withSelfRel());
    }

    @PutMapping("{projectId}/collaborators/{username}")
    public void addCollaborator(@PathVariable Long projectId, @PathVariable String username) {
        projectService.addProjectCollaborator(projectId, username);
    }

    @DeleteMapping("{projectId}/collaborators/{username}")
    public void removeCollaborator(@PathVariable Long projectId, @PathVariable String username) {
        projectService.removeProjectCollaborator(projectId, username);
    }

    @GetMapping("{projectId}/bugs")
    public CollectionModel<EntityModel<BugResponseDto>> getAllBugs(
            @PathVariable Long projectId,
            @PageableDefault(page = 0, size = 15) Pageable pageable) {
        Page<BugResponseDto> bugsPage = bugService.findAllBugsByProjectId(projectId, pageable).map(bugDtoMapper::toDto);
        return bugDtoPagedResourcesAssembler.toModel(bugsPage, bugDtoModelAssembler);
    }
}
