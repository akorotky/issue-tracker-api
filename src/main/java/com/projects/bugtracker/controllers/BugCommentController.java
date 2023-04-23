package com.projects.bugtracker.controllers;

import com.projects.bugtracker.assemblers.BugCommentModelAssembler;
import com.projects.bugtracker.dto.BugCommentDto;
import com.projects.bugtracker.security.CurrentUser;
import com.projects.bugtracker.security.UserPrincipal;
import com.projects.bugtracker.services.BugCommentService;
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
@RequestMapping("api/comments")
@RequiredArgsConstructor
public class BugCommentController {

    private final BugCommentService bugCommentService;
    private final BugCommentModelAssembler bugCommentModelAssembler;
    private final PagedResourcesAssembler<BugCommentDto> pagedResourcesAssembler;

    @GetMapping
    public PagedModel<EntityModel<BugCommentDto>> getBugCommentsPage(@PageableDefault(page = 0, size = 15) Pageable pageable) {
        Page<BugCommentDto> commentsPage = bugCommentService.findAllBugComments(pageable);
        return pagedResourcesAssembler.toModel(commentsPage, bugCommentModelAssembler);
    }

    @GetMapping("{commentId}")
    public EntityModel<BugCommentDto> getBugComment(@PathVariable Long commentId) {
        BugCommentDto bugCommentDto = bugCommentService.findBugCommentById(commentId);
        return bugCommentModelAssembler.toModel(bugCommentDto);
    }

    @PostMapping
    public void createBugComment(
            @Valid @RequestBody BugCommentDto bugCommentDto,
            @CurrentUser UserPrincipal currentUser) {
        bugCommentService.createBugComment(bugCommentDto, currentUser.getUser());
    }

    @DeleteMapping("{commentId}")
    public void deleteBugComment(@PathVariable Long commentId) {
        bugCommentService.deleteBugCommentById(commentId);
    }
}
