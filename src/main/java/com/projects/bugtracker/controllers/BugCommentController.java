package com.projects.bugtracker.controllers;

import com.projects.bugtracker.assemblers.ModelAssembler;
import com.projects.bugtracker.dto.bugcommentdto.BugCommentDtoMapper;
import com.projects.bugtracker.dto.bugcommentdto.BugCommentRequestDto;
import com.projects.bugtracker.dto.bugcommentdto.BugCommentResponseDto;
import com.projects.bugtracker.security.principal.CurrentUser;
import com.projects.bugtracker.security.principal.UserPrincipal;
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
    private final ModelAssembler<BugCommentResponseDto> bugCommentDtoModelAssembler;
    private final PagedResourcesAssembler<BugCommentResponseDto> bugCommentDtoPagedResourcesAssembler;
    private final BugCommentDtoMapper bugCommentDtoMapper;

    @GetMapping
    public PagedModel<EntityModel<BugCommentResponseDto>> getBugCommentsPage(@PageableDefault(page = 0, size = 15) Pageable pageable) {
        Page<BugCommentResponseDto> commentsPage = bugCommentService.findAllBugComments(pageable).map(bugCommentDtoMapper::toDto);
        return bugCommentDtoPagedResourcesAssembler.toModel(commentsPage, bugCommentDtoModelAssembler);
    }

    @GetMapping("{commentId}")
    public EntityModel<BugCommentResponseDto> getBugComment(@PathVariable Long commentId) {
        BugCommentResponseDto bugCommentDto = bugCommentDtoMapper.toDto(bugCommentService.findBugCommentById(commentId));
        return bugCommentDtoModelAssembler.toModel(bugCommentDto);
    }

    @PostMapping
    public void createBugComment(
            @Valid @RequestBody BugCommentRequestDto bugCommentRequestDto,
            @CurrentUser UserPrincipal currentUser) {
        bugCommentService.createBugComment(bugCommentRequestDto, currentUser.getUser());
    }

    @DeleteMapping("{commentId}")
    public void deleteBugComment(@PathVariable Long commentId) {
        bugCommentService.deleteBugCommentById(commentId);
    }
}
