package com.bugtracker.api.controllers;

import com.bugtracker.api.assemblers.ModelAssembler;
import com.bugtracker.api.dto.bugcomment.BugCommentDtoMapper;
import com.bugtracker.api.dto.bugcomment.BugCommentResponseDto;
import com.bugtracker.api.dto.bug.BugDtoMapper;
import com.bugtracker.api.dto.bug.BugRequestDto;
import com.bugtracker.api.dto.bug.BugResponseDto;
import com.bugtracker.api.entities.Bug;
import com.bugtracker.api.entities.Project;
import com.bugtracker.api.security.principal.CurrentUser;
import com.bugtracker.api.services.BugCommentService;
import com.bugtracker.api.services.BugService;
import com.bugtracker.api.security.principal.UserPrincipal;
import com.bugtracker.api.services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/bugs")
@RequiredArgsConstructor
public class BugController {

    private final BugService bugService;
    private final BugCommentService bugCommentService;
    private final ProjectService projectService;
    private final BugDtoMapper bugDtoMapper;
    private final BugCommentDtoMapper bugCommentDtoMapper;
    private final ModelAssembler<BugResponseDto> bugDtoModelAssembler;
    private final ModelAssembler<BugCommentResponseDto> bugCommentDtoModelAssembler;
    private final PagedResourcesAssembler<BugResponseDto> bugDtoPagedResourcesAssembler;
    private final PagedResourcesAssembler<BugCommentResponseDto> bugCommentDtoPagedResourcesAssembler;

    @GetMapping
    public PagedModel<EntityModel<BugResponseDto>> getBugsPage(@PageableDefault(page = 0, size = 15) Pageable pageable) {
        Page<BugResponseDto> bugsPage = bugService.findAllBugs(pageable).map(bugDtoMapper::toDto);
        return bugDtoPagedResourcesAssembler.toModel(bugsPage, bugDtoModelAssembler);
    }

    @GetMapping("{bugId}")
    public EntityModel<BugResponseDto> getBug(@PathVariable Long bugId) {
        BugResponseDto bug = bugDtoMapper.toDto(bugService.findBugById(bugId));
        return bugDtoModelAssembler.toModel(bug);
    }

    @PostMapping
    public void createBug(@Valid @RequestBody BugRequestDto bugRequestDto, @CurrentUser UserPrincipal currentUser) {
        Project project = projectService.findProjectById(bugRequestDto.projectId());
        bugService.createBug(project, bugRequestDto, currentUser.user());
    }

    @PatchMapping("{bugId}")
    public void updateBug(@PathVariable Long bugId, @Valid @RequestBody BugRequestDto bugRequestDto) {
        Bug bug = bugService.findBugById(bugId);
        bugService.updateBug(bug, bugRequestDto);
    }

    @DeleteMapping("{bugId}")
    public void deleteBug(@PathVariable Long bugId) {
        Bug bug = bugService.findBugById(bugId);
        bugService.deleteBug(bug);
    }

    @GetMapping("{bugId}/comments")
    public PagedModel<EntityModel<BugCommentResponseDto>> getComments(
            @PathVariable Long bugId,
            @PageableDefault(page = 0, size = 15) Pageable pageable) {
        Bug bug = bugService.findBugById(bugId);
        Page<BugCommentResponseDto> commentsPage = bugCommentService.findAllCommentsByBug(bug, pageable).map(bugCommentDtoMapper::toDto);
        return bugCommentDtoPagedResourcesAssembler.toModel(commentsPage, bugCommentDtoModelAssembler);
    }
}
