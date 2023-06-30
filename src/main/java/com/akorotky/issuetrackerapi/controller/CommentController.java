package com.akorotky.issuetrackerapi.controller;

import com.akorotky.issuetrackerapi.dto.comment.CommentDtoMapper;
import com.akorotky.issuetrackerapi.dto.comment.CommentRequestDto;
import com.akorotky.issuetrackerapi.dto.comment.CommentResponseDto;
import com.akorotky.issuetrackerapi.entity.Issue;
import com.akorotky.issuetrackerapi.entity.Comment;
import com.akorotky.issuetrackerapi.rest.RestModelAssembler;
import com.akorotky.issuetrackerapi.security.principal.CurrentUser;
import com.akorotky.issuetrackerapi.security.principal.UserPrincipal;
import com.akorotky.issuetrackerapi.service.CommentService;
import com.akorotky.issuetrackerapi.service.IssueService;
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
@RequestMapping("api/comments")
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;
    private final IssueService issueService;
    private final RestModelAssembler<CommentResponseDto> commentDtoRestModelAssembler;
    private final PagedResourcesAssembler<CommentResponseDto> commentDtoPagedResourcesAssembler;
    private final CommentDtoMapper commentDtoMapper;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<PagedModel<EntityModel<CommentResponseDto>>> searchComments(@PageableDefault(size = 15) Pageable pageable) {
        var commentDtosPage = commentService.findAllComments(pageable).map(commentDtoMapper::toDto);
        var commentDtosPagedModel = commentDtoPagedResourcesAssembler.toModel(commentDtosPage, commentDtoRestModelAssembler);
        return ResponseEntity.ok(commentDtosPagedModel);
    }

    @GetMapping(path = "{commentId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CommentResponseDto>> getComment(@PathVariable Long commentId) {
        CommentResponseDto commentDto = commentDtoMapper.toDto(commentService.findCommentById(commentId));
        var commentDtoModel = commentDtoRestModelAssembler.toModel(commentDto);
        return ResponseEntity.ok(commentDtoModel);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createComment(
            @Valid @RequestBody CommentRequestDto commentRequestDto,
            @CurrentUser UserPrincipal currentUser) {
        Issue issue = issueService.findIssueById(commentRequestDto.issueId());
        Long commentId = commentService.createComment(issue, commentRequestDto, currentUser.user()).getId();
        URI createdCommentUri = linkTo(methodOn(CommentController.class).getComment(commentId)).toUri();
        return ResponseEntity.created(createdCommentUri).build();
    }

    @PatchMapping(path = "{commentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateComment(@PathVariable Long issueId, @Valid @RequestBody CommentRequestDto commentRequestDto) {
        Comment comment = commentService.findCommentById(issueId);
        commentService.updateComment(comment, commentRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        Comment comment = commentService.findCommentById(commentId);
        commentService.deleteComment(comment);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
