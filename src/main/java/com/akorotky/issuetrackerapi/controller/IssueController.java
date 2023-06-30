package com.akorotky.issuetrackerapi.controller;

import com.akorotky.issuetrackerapi.dto.issue.IssueDtoMapper;
import com.akorotky.issuetrackerapi.dto.issue.IssueRequestDto;
import com.akorotky.issuetrackerapi.dto.comment.CommentDtoMapper;
import com.akorotky.issuetrackerapi.dto.comment.CommentResponseDto;
import com.akorotky.issuetrackerapi.entity.Issue;
import com.akorotky.issuetrackerapi.entity.Project;
import com.akorotky.issuetrackerapi.rest.RestModelAssembler;
import com.akorotky.issuetrackerapi.security.principal.CurrentUser;
import com.akorotky.issuetrackerapi.security.principal.UserPrincipal;
import com.akorotky.issuetrackerapi.service.CommentService;
import com.akorotky.issuetrackerapi.service.IssueService;
import com.akorotky.issuetrackerapi.service.ProjectService;
import com.akorotky.issuetrackerapi.dto.issue.IssueResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/issues")
@RequiredArgsConstructor
@Validated
public class IssueController {

    private final IssueService issueService;
    private final CommentService commentService;
    private final ProjectService projectService;
    private final IssueDtoMapper issueDtoMapper;
    private final CommentDtoMapper commentDtoMapper;
    private final RestModelAssembler<IssueResponseDto> issueDtoRestModelAssembler;
    private final RestModelAssembler<CommentResponseDto> issueCommentDtoRestModelAssembler;
    private final PagedResourcesAssembler<IssueResponseDto> issueDtoPagedResourcesAssembler;
    private final PagedResourcesAssembler<CommentResponseDto> issueCommentDtoPagedResourcesAssembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<PagedModel<EntityModel<IssueResponseDto>>> searchIssues(@PageableDefault(size = 15) Pageable pageable) {
        var issues = issueService.findAllIssues(pageable).map(issueDtoMapper::toDto);
        var issuesPagedModel = issueDtoPagedResourcesAssembler.toModel(issues, issueDtoRestModelAssembler);
        return ResponseEntity.ok(issuesPagedModel);
    }

    @GetMapping(path = "{issueId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<IssueResponseDto>> getIssue(@PathVariable Long issueId) {
        IssueResponseDto issue = issueDtoMapper.toDto(issueService.findIssueById(issueId));
        var issueDtoModel = issueDtoRestModelAssembler.toModel(issue);
        return ResponseEntity.ok(issueDtoModel);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createIssue(@Valid @RequestBody IssueRequestDto issueRequestDto, @CurrentUser UserPrincipal currentUser) {
        Project project = projectService.findProjectById(issueRequestDto.projectId());
        Long issueId = issueService.createIssue(project, issueRequestDto, currentUser.user()).getId();
        URI createdIssueUri = linkTo(methodOn(IssueController.class).getIssue(issueId)).toUri();
        return ResponseEntity.created(createdIssueUri).build();
    }

    @PatchMapping(path = "{issueId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateIssue(@PathVariable Long issueId, @Valid @RequestBody IssueRequestDto issueRequestDto) {
        Issue issue = issueService.findIssueById(issueId);
        issueService.updateIssue(issue, issueRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{issueId}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long issueId) {
        Issue issue = issueService.findIssueById(issueId);
        issueService.deleteIssue(issue);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "{issueId}/comments", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<PagedModel<EntityModel<CommentResponseDto>>> getIssueComments(
            @PathVariable Long issueId,
            @PageableDefault(size = 15) Pageable pageable) {
        Issue issue = issueService.findIssueById(issueId);
        var commentDtosPage = commentService.findAllCommentsByIssue(issue, pageable).map(commentDtoMapper::toDto);
        var commentDtosPagedModel = issueCommentDtoPagedResourcesAssembler.toModel(commentDtosPage, issueCommentDtoRestModelAssembler);
        return ResponseEntity.ok(commentDtosPagedModel);
    }
}
