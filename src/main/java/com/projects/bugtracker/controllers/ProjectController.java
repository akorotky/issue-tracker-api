package com.projects.bugtracker.controllers;

import com.projects.bugtracker.assemblers.BugModelAssembler;
import com.projects.bugtracker.dto.BugDto;
import com.projects.bugtracker.dto.ProjectDto;
import com.projects.bugtracker.assemblers.ProjectModelAssembler;
import com.projects.bugtracker.assemblers.UserModelAssembler;
import com.projects.bugtracker.dto.UserDto;
import com.projects.bugtracker.security.CurrentUser;
import com.projects.bugtracker.security.UserPrincipal;
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
    private final ProjectModelAssembler projectModelAssembler;
    private final UserModelAssembler userAssembler;
    private final BugModelAssembler bugModelAssembler;
    private final PagedResourcesAssembler<ProjectDto> pagedProjectResourcesAssembler;
    private final PagedResourcesAssembler<BugDto> pagedBugResourcesAssembler;

    @GetMapping
    public PagedModel<EntityModel<ProjectDto>> getProjectsPage(
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) String collaborator,
            @PageableDefault(page = 0, size = 20) Pageable pageable) {
        Page<ProjectDto> projectsPage;

        if (owner == null && collaborator == null) projectsPage = projectService.findAllProjects(pageable);
        else if (owner != null) projectsPage = projectService.findAllProjectsByOwner(owner, pageable);
        else projectsPage = projectService.findAllProjectsByCollaborator(collaborator, pageable);

        return pagedProjectResourcesAssembler.toModel(projectsPage, projectModelAssembler);
    }

    @GetMapping("{projectId}")
    public EntityModel<ProjectDto> getProject(@PathVariable Long projectId) {
        ProjectDto project = projectService.findProjectById(projectId);
        return projectModelAssembler.toModel(project);
    }

    @PatchMapping("{projectId}")
    public void updateProject(@RequestBody ProjectDto project, @PathVariable Long projectId) {
        projectService.updateProject(project, projectId);
    }

    @DeleteMapping("{projectId}")
    public void deleteProject(@PathVariable Long projectId) {
        projectService.deleteProjectById(projectId);
    }

    @PostMapping
    public void createProject(@RequestBody ProjectDto project, @CurrentUser UserPrincipal currentUser) {
        projectService.createProject(project, currentUser.getUser());
    }

    @GetMapping("{projectId}/collaborators")
    public CollectionModel<EntityModel<UserDto>> getAllCollaborators(@PathVariable Long projectId) {
        List<EntityModel<UserDto>> collaborators = projectService.getProjectCollaborators(projectId).stream()
                .map(userAssembler::toModel)
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
    public CollectionModel<EntityModel<BugDto>> getAllBugs(
            @PathVariable Long projectId,
            @PageableDefault(page = 0, size = 15) Pageable pageable) {

        Page<BugDto> bugsPage = projectService.findAllBugsByProjectId(projectId, pageable);

        return pagedBugResourcesAssembler.toModel(bugsPage, bugModelAssembler);
    }
}
