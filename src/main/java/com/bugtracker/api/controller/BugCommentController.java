package com.bugtracker.api.controller;

import com.bugtracker.api.rest.RestModelAssembler;
import com.bugtracker.api.dto.bugcomment.BugCommentDtoMapper;
import com.bugtracker.api.dto.bugcomment.BugCommentRequestDto;
import com.bugtracker.api.dto.bugcomment.BugCommentResponseDto;
import com.bugtracker.api.entity.Bug;
import com.bugtracker.api.entity.BugComment;
import com.bugtracker.api.security.principal.CurrentUser;
import com.bugtracker.api.security.principal.UserPrincipal;
import com.bugtracker.api.service.BugCommentService;
import com.bugtracker.api.service.BugService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
@RequestMapping("api/comments")
@RequiredArgsConstructor
@Validated
public class BugCommentController {

    private final BugCommentService bugCommentService;
    private final BugService bugService;
    private final RestModelAssembler<BugCommentResponseDto> bugCommentDtoRestModelAssembler;
    private final PagedResourcesAssembler<BugCommentResponseDto> bugCommentDtoPagedResourcesAssembler;
    private final BugCommentDtoMapper bugCommentDtoMapper;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<PagedModel<EntityModel<BugCommentResponseDto>>> getBugCommentsPage(@PageableDefault(size = 15) Pageable pageable) {
        var commentDtosPage = bugCommentService.findAllBugComments(pageable).map(bugCommentDtoMapper::toDto);
        var commentDtosPagedModel = bugCommentDtoPagedResourcesAssembler.toModel(commentDtosPage, bugCommentDtoRestModelAssembler);
        return ResponseEntity.ok(commentDtosPagedModel);
    }

    @GetMapping(path = "{commentId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<BugCommentResponseDto>> getBugComment(@PathVariable Long commentId) {
        BugCommentResponseDto bugCommentDto = bugCommentDtoMapper.toDto(bugCommentService.findBugCommentById(commentId));
        var bugCommentDtoModel = bugCommentDtoRestModelAssembler.toModel(bugCommentDto);
        return ResponseEntity.ok(bugCommentDtoModel);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createBugComment(
            @Valid @RequestBody BugCommentRequestDto bugCommentRequestDto,
            @CurrentUser UserPrincipal currentUser) {
        Bug bug = bugService.findBugById(bugCommentRequestDto.bugId());
        Long bugCommentId = bugCommentService.createBugComment(bug, bugCommentRequestDto, currentUser.user()).getId();
        URI createdBugCommentUri = linkTo(methodOn(BugCommentController.class).getBugComment(bugCommentId)).toUri();
        return ResponseEntity.created(createdBugCommentUri).build();
    }

    @PatchMapping(path = "{commentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
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
