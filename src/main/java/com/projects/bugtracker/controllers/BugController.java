package com.projects.bugtracker.controllers;

import com.projects.bugtracker.assemblers.ModelAssembler;
import com.projects.bugtracker.dto.bugcommentdto.BugCommentDtoMapper;
import com.projects.bugtracker.dto.bugcommentdto.BugCommentResponseDto;
import com.projects.bugtracker.dto.bugdto.BugDtoMapper;
import com.projects.bugtracker.dto.bugdto.BugRequestDto;
import com.projects.bugtracker.dto.bugdto.BugResponseDto;
import com.projects.bugtracker.security.principal.CurrentUser;
import com.projects.bugtracker.security.principal.UserPrincipal;
import com.projects.bugtracker.services.BugCommentService;
import com.projects.bugtracker.services.BugService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/bugs")
@RequiredArgsConstructor
public class BugController {

    private final BugService bugService;
    private final BugCommentService bugCommentService;
    private final BugDtoMapper bugDtoMapper;
    private final BugCommentDtoMapper bugCommentDtoMapper;
    private final ModelAssembler<BugResponseDto> bugDtoModelAssembler;
    private final ModelAssembler<BugCommentResponseDto> bugCommentDtoModelAssembler;
    private final PagedResourcesAssembler<BugResponseDto> bugDtoPagedResourcesAssembler;
    private final PagedResourcesAssembler<BugCommentResponseDto> bugCommentDtoPagedResourcesAssembler;

    @GetMapping
    public CollectionModel<EntityModel<BugResponseDto>> getBugsPage(@PageableDefault(page = 0, size = 15) Pageable pageable) {
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
        bugService.createBug(bugRequestDto, currentUser.getUser());
    }

    @DeleteMapping("{bugId}")
    public void deleteBug(@PathVariable Long bugId) {
        bugService.deleteBugById(bugId);
    }

    @GetMapping("{bugId}/comments")
    public PagedModel<EntityModel<BugCommentResponseDto>> getComments(
            @PathVariable Long bugId,
            @PageableDefault(page = 0, size = 15) Pageable pageable) {
        Page<BugCommentResponseDto> commentsPage = bugCommentService.findAllCommentsByBugId(bugId, pageable).map(bugCommentDtoMapper::toDto);
        return bugCommentDtoPagedResourcesAssembler.toModel(commentsPage, bugCommentDtoModelAssembler);
    }
}
