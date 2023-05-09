package com.projects.bugtracker.controllers;

import com.projects.bugtracker.assemblers.ModelAssembler;
import com.projects.bugtracker.dto.BugCommentDto;
import com.projects.bugtracker.dto.BugDto;
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
    private final ModelAssembler<BugDto> bugDtoModelAssembler;
    private final ModelAssembler<BugCommentDto> bugCommentDtoModelAssembler;
    private final PagedResourcesAssembler<BugDto> bugDtoPagedResourcesAssembler;
    private final PagedResourcesAssembler<BugCommentDto> bugCommentDtoPagedResourcesAssembler;

    @GetMapping
    public CollectionModel<EntityModel<BugDto>> getBugsPage(@PageableDefault(page = 0, size = 15) Pageable pageable) {
        Page<BugDto> bugsPage = bugService.findAllBugs(pageable);
        return bugDtoPagedResourcesAssembler.toModel(bugsPage, bugDtoModelAssembler);
    }

    @GetMapping("{bugId}")
    public EntityModel<BugDto> getBug(@PathVariable Long bugId) {
        BugDto bug = bugService.findBugById(bugId);
        return bugDtoModelAssembler.toModel(bug);
    }

    @PostMapping
    public void createBug(@Valid @RequestBody BugDto bugDto, @CurrentUser UserPrincipal currentUser) {
        bugService.createBug(bugDto, currentUser.getUser());
    }

    @DeleteMapping("{bugId}")
    public void deleteBug(@PathVariable Long bugId) {
        bugService.deleteBugById(bugId);
    }

    @GetMapping("{bugId}/comments")
    public PagedModel<EntityModel<BugCommentDto>> getComments(
            @PathVariable Long bugId,
            @PageableDefault(page = 0, size = 15) Pageable pageable) {
        Page<BugCommentDto> commentsPage = bugCommentService.findAllCommentsByBugId(bugId, pageable);
        return bugCommentDtoPagedResourcesAssembler.toModel(commentsPage, bugCommentDtoModelAssembler);
    }
}
