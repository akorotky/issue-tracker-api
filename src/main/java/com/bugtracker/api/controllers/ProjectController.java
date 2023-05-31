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
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<PagedModel<EntityModel<ProjectResponseDto>>> getProjectsPage(
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
        Page<ProjectResponseDto> projectDtosPage = projectsPage.map(projectDtoMapper::toDto);
        PagedModel<EntityModel<ProjectResponseDto>> projectDtosPagedModel = projectDtoPagedResourcesAssembler.toModel(projectDtosPage, projectDtoModelAssembler);
        return ResponseEntity.ok(projectDtosPagedModel);
    }

    @GetMapping(path = "{projectId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProjectResponseDto>> getProject(@PathVariable Long projectId) {
        Project project = projectService.findProjectById(projectId);
        ProjectResponseDto projectDto = projectDtoMapper.toDto(project);
        EntityModel<ProjectResponseDto> projectDtoModel = projectDtoModelAssembler.toModel(projectDto);
        return ResponseEntity.ok(projectDtoModel);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createProject(@RequestBody ProjectRequestDto projectRequestDto, @CurrentUser UserPrincipal currentUser) {
        Project project = projectService.createProject(projectRequestDto, currentUser.user());
        URI createdProjectUri = linkTo(methodOn(ProjectController.class).getProject(project.getId())).toUri();
        return ResponseEntity.created(createdProjectUri).build();
    }

    @PatchMapping(path = "{projectId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateProject(@RequestBody ProjectRequestDto projectRequestDto, @PathVariable Long projectId) {
        Project project = projectService.findProjectById(projectId);
        projectService.updateProject(project, projectRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        Project project = projectService.findProjectById(projectId);
        projectService.deleteProject(project);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "{projectId}/collaborators", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<UserResponseDto>>> getAllCollaborators(@PathVariable Long projectId) {
        Project project = projectService.findProjectById(projectId);
        List<EntityModel<UserResponseDto>> collaboratorDtoList = projectService.getProjectCollaborators(project).stream()
                .map(userDtoMapper::toDto)
                .map(userDtoModelAssembler::toModel)
                .toList();

        CollectionModel<EntityModel<UserResponseDto>> collaboratorDtoListModel = CollectionModel.of(collaboratorDtoList);
        collaboratorDtoListModel.add(linkTo(methodOn(ProjectController.class).getAllCollaborators(projectId)).withSelfRel());
        return ResponseEntity.ok(collaboratorDtoListModel);
    }

    @PutMapping("{projectId}/collaborators/{username}")
    public ResponseEntity<Void> addCollaborator(@PathVariable Long projectId, @PathVariable String username) {
        Project project = projectService.findProjectById(projectId);
        User collaborator = userService.findUserByUsername(username);
        projectService.addProjectCollaborator(project, collaborator);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{projectId}/collaborators/{username}")
    public ResponseEntity<Void> removeCollaborator(@PathVariable Long projectId, @PathVariable String username) {
        Project project = projectService.findProjectById(projectId);
        User collaborator = userService.findUserByUsername(username);
        projectService.removeProjectCollaborator(project, collaborator);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "{projectId}/bugs", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<PagedModel<EntityModel<BugResponseDto>>> getAllBugs(
            @PathVariable Long projectId,
            @PageableDefault(page = 0, size = 15) Pageable pageable) {
        Project project = projectService.findProjectById(projectId);
        Page<BugResponseDto> bugDtosPage = bugService.findAllBugsByProject(project, pageable).map(bugDtoMapper::toDto);
        PagedModel<EntityModel<BugResponseDto>> bugDtosPagedModel = bugDtoPagedResourcesAssembler.toModel(bugDtosPage, bugDtoModelAssembler);
        return ResponseEntity.ok(bugDtosPagedModel);
    }
}
