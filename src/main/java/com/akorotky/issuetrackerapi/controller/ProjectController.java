package com.akorotky.issuetrackerapi.controller;

import com.akorotky.issuetrackerapi.dto.issue.IssueDtoMapper;
import com.akorotky.issuetrackerapi.dto.project.ProjectDtoMapper;
import com.akorotky.issuetrackerapi.dto.project.ProjectRequestDto;
import com.akorotky.issuetrackerapi.dto.project.ProjectResponseDto;
import com.akorotky.issuetrackerapi.dto.user.UserDtoMapper;
import com.akorotky.issuetrackerapi.dto.user.UserResponseDto;
import com.akorotky.issuetrackerapi.entity.Project;
import com.akorotky.issuetrackerapi.entity.User;
import com.akorotky.issuetrackerapi.rest.RestModelAssembler;
import com.akorotky.issuetrackerapi.security.principal.CurrentUser;
import com.akorotky.issuetrackerapi.security.principal.UserPrincipal;
import com.akorotky.issuetrackerapi.service.IssueService;
import com.akorotky.issuetrackerapi.service.ProjectService;
import com.akorotky.issuetrackerapi.service.UserService;
import com.akorotky.issuetrackerapi.dto.issue.IssueResponseDto;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private final IssueService issueService;
    private final IssueDtoMapper issueDtoMapper;
    private final UserDtoMapper userDtoMapper;
    private final ProjectDtoMapper projectDtoMapper;
    private final RestModelAssembler<ProjectResponseDto> projectDtoRestModelAssembler;
    private final RestModelAssembler<UserResponseDto> userDtoRestModelAssembler;
    private final RestModelAssembler<IssueResponseDto> issueDtoRestModelAssembler;
    private final PagedResourcesAssembler<ProjectResponseDto> projectDtoPagedResourcesAssembler;
    private final PagedResourcesAssembler<IssueResponseDto> issueDtoPagedResourcesAssembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<PagedModel<EntityModel<ProjectResponseDto>>> getProjectsPage(
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) String collaborator,
            @PageableDefault(size = 20) Pageable pageable) {
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
        var projectDtosPage = projectsPage.map(projectDtoMapper::toDto);
        var projectDtosPagedModel = projectDtoPagedResourcesAssembler.toModel(projectDtosPage, projectDtoRestModelAssembler);
        return ResponseEntity.ok(projectDtosPagedModel);
    }

    @GetMapping(path = "{projectId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProjectResponseDto>> getProject(@PathVariable Long projectId) {
        Project project = projectService.findProjectById(projectId);
        ProjectResponseDto projectDto = projectDtoMapper.toDto(project);
        var projectDtoModel = projectDtoRestModelAssembler.toModel(projectDto);
        return ResponseEntity.ok(projectDtoModel);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createProject(@RequestBody ProjectRequestDto projectRequestDto, @CurrentUser UserPrincipal currentUser) {
        Long projectId = projectService.createProject(projectRequestDto, currentUser.user()).getId();
        URI createdProjectUri = linkTo(methodOn(ProjectController.class).getProject(projectId)).toUri();
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
        var collaboratorDtoList = projectService.getProjectCollaborators(project).stream()
                .map(userDtoMapper::toDto)
                .map(userDtoRestModelAssembler::toModel)
                .toList();
        var collaboratorDtoListModel = CollectionModel.of(collaboratorDtoList);
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
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "{projectId}/issues", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<PagedModel<EntityModel<IssueResponseDto>>> getAllIssues(
            @PathVariable Long projectId,
            @PageableDefault(size = 15) Pageable pageable) {
        Project project = projectService.findProjectById(projectId);
        var issueDtos = issueService.findAllIssuesByProject(project, pageable).map(issueDtoMapper::toDto);
        var issueDtosdModel = issueDtoPagedResourcesAssembler.toModel(issueDtos, issueDtoRestModelAssembler);
        return ResponseEntity.ok(issueDtosdModel);
    }
}
