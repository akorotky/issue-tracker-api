package com.akorotky.issuetrackerapi.rest.impl;

import com.akorotky.issuetrackerapi.controller.UserController;
import com.akorotky.issuetrackerapi.rest.RestModelAssembler;
import com.akorotky.issuetrackerapi.controller.IssueController;
import com.akorotky.issuetrackerapi.controller.ProjectController;
import com.akorotky.issuetrackerapi.dto.issue.IssueResponseDto;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class IssueDtoRestModelAssembler implements RestModelAssembler<IssueResponseDto> {

    @Override
    public @NonNull EntityModel<IssueResponseDto> toModel(IssueResponseDto issueResponseDto) {
        Long issueId = issueResponseDto.id();
        String username = issueResponseDto.author().username();
        Long projectId = issueResponseDto.projectId();

        return EntityModel.of(issueResponseDto,
                linkTo(methodOn(IssueController.class).getIssue(issueId)).withSelfRel(),
                linkTo(methodOn(UserController.class).getUser(username)).withRel("author"),
                linkTo(methodOn(ProjectController.class).getProject(projectId)).withRel("project"),
                linkTo(methodOn(IssueController.class).getIssueComments(issueId, Pageable.unpaged())).withRel("comments").expand());
    }
}
