package com.projects.bugtracker.controllers;

import com.projects.bugtracker.assemblers.BugModelAssembler;
import com.projects.bugtracker.dto.BugDto;
import com.projects.bugtracker.dto.ProjectDto;
import com.projects.bugtracker.assemblers.ProjectModelAssembler;
import com.projects.bugtracker.assemblers.UserModelAssembler;
import com.projects.bugtracker.dto.UserDto;
import com.projects.bugtracker.security.CurrentUser;
import com.projects.bugtracker.security.UserPrincipal;
import com.projects.bugtracker.services.BugService;
import com.projects.bugtracker.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectModelAssembler projectAssembler;
    private final UserModelAssembler userAssembler;
    private final BugModelAssembler bugAssembler;

    @GetMapping
    public CollectionModel<EntityModel<ProjectDto>> getAllProjects(
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) String collaborator) {

        List<ProjectDto> projects = new ArrayList<>();
        if (owner == null && collaborator == null) projects.addAll(projectService.findAllProjects());
        else if (owner != null) projects.addAll(projectService.findAllProjectsByOwner(owner));
        else projects.addAll(projectService.findAllProjectsByCollaborator(collaborator));

        List<EntityModel<ProjectDto>> projectModels = projects.stream()
                .map(projectAssembler::toModel)
                .toList();

        return CollectionModel.of(projectModels, linkTo(methodOn(ProjectController.class).getAllProjects(owner, collaborator)).withSelfRel().expand());
    }

    @GetMapping("{projectId}")
    public EntityModel<ProjectDto> getProject(@PathVariable Long projectId) {
        ProjectDto project = projectService.findProjectById(projectId);
        return projectAssembler.toModel(project);
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
    public CollectionModel<EntityModel<BugDto>> getAllBugs(@PathVariable Long projectId) {
        List<EntityModel<BugDto>> bugs = projectService.findAllBugs(projectId).stream()
                .map(bugAssembler::toModel)
                .toList();

        return CollectionModel.of(bugs, linkTo(methodOn(ProjectController.class).getAllBugs(projectId)).withSelfRel());
    }
}
