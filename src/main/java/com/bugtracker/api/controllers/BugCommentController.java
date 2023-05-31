package com.bugtracker.api.controllers;

import com.bugtracker.api.assemblers.ModelAssembler;
import com.bugtracker.api.dto.bugcomment.BugCommentDtoMapper;
import com.bugtracker.api.dto.bugcomment.BugCommentRequestDto;
import com.bugtracker.api.dto.bugcomment.BugCommentResponseDto;
import com.bugtracker.api.entities.Bug;
import com.bugtracker.api.entities.BugComment;
import com.bugtracker.api.security.principal.CurrentUser;
import com.bugtracker.api.services.BugCommentService;
import com.bugtracker.api.security.principal.UserPrincipal;
import com.bugtracker.api.services.BugService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/comments")
@RequiredArgsConstructor
public class BugCommentController {

    private final BugCommentService bugCommentService;
    private final BugService bugService;
    private final ModelAssembler<BugCommentResponseDto> bugCommentDtoModelAssembler;
    private final PagedResourcesAssembler<BugCommentResponseDto> bugCommentDtoPagedResourcesAssembler;
    private final BugCommentDtoMapper bugCommentDtoMapper;

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<BugCommentResponseDto>>> getBugCommentsPage(@PageableDefault(page = 0, size = 15) Pageable pageable) {
        Page<BugCommentResponseDto> commentDtosPage = bugCommentService.findAllBugComments(pageable).map(bugCommentDtoMapper::toDto);
        PagedModel<EntityModel<BugCommentResponseDto>> commentDtosPagedModel = bugCommentDtoPagedResourcesAssembler.toModel(commentDtosPage, bugCommentDtoModelAssembler);
        return ResponseEntity.ok(commentDtosPagedModel);
    }

    @GetMapping("{commentId}")
    public ResponseEntity<EntityModel<BugCommentResponseDto>> getBugComment(@PathVariable Long commentId) {
        BugCommentResponseDto bugCommentDto = bugCommentDtoMapper.toDto(bugCommentService.findBugCommentById(commentId));
        EntityModel<BugCommentResponseDto> bugCommentDtoModel = bugCommentDtoModelAssembler.toModel(bugCommentDto);
        return ResponseEntity.ok(bugCommentDtoModel);
    }

    @PostMapping
    public ResponseEntity<Void> createBugComment(
            @Valid @RequestBody BugCommentRequestDto bugCommentRequestDto,
            @CurrentUser UserPrincipal currentUser) {
        Bug bug = bugService.findBugById(bugCommentRequestDto.bugId());
        BugComment bugComment = bugCommentService.createBugComment(bug, bugCommentRequestDto, currentUser.user());
        URI createdBugCommentUri = linkTo(methodOn(BugCommentController.class).getBugComment(bugComment.getId())).toUri();
        return ResponseEntity.created(createdBugCommentUri).build();
    }

    @PatchMapping("{commentId}")
    public ResponseEntity<Void> updateBugComment(@PathVariable Long bugId, @Valid @RequestBody BugCommentRequestDto bugCommentRequestDto) {
        BugComment bugComment = bugCommentService.findBugCommentById(bugId);
        bugCommentService.updateBugComment(bugComment, bugCommentRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<Void> deleteBugComment(@PathVariable Long commentId) {
        BugComment bugComment = bugCommentService.findBugCommentById(commentId);
        bugCommentService.deleteBugComment(bugComment);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
