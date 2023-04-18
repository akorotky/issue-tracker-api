package com.projects.bugtracker.controllers;

import com.projects.bugtracker.assemblers.BugModelAssembler;
import com.projects.bugtracker.dto.BugDto;
import com.projects.bugtracker.security.CurrentUser;
import com.projects.bugtracker.security.UserPrincipal;
import com.projects.bugtracker.services.BugService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/bugs")
public class BugController {

    private final BugService bugService;
    private final BugModelAssembler bugAssembler;

    @GetMapping
    public CollectionModel<EntityModel<BugDto>> getAllBugs(@RequestParam(name = "project", required = false) Long projectId) {
        List<BugDto> bugs = new ArrayList<>();

        if (projectId != null) bugs.addAll(bugService.findAllBugsByProject(projectId));
        else bugs.addAll(bugService.findAllBugs());

        List<EntityModel<BugDto>> bugModels = bugs.stream()
                .map(bugAssembler::toModel)
                .toList();

        return CollectionModel.of(bugModels, linkTo(methodOn(BugController.class).getAllBugs(projectId)).withSelfRel().expand());
    }

    @GetMapping("{bugId}")
    public EntityModel<BugDto> getBug(@PathVariable Long bugId) {
        BugDto bug = bugService.findBugById(bugId);
        return bugAssembler.toModel(bug);
    }

    @PostMapping
    public void createBug(@Valid @RequestBody BugDto bugDto, @CurrentUser UserPrincipal currentUser) {
        bugService.createBug(bugDto, currentUser.getUser());
    }

    @DeleteMapping("{bugId}")
    public void deleteBug(@PathVariable Long bugId) {
        bugService.deleteBugById(bugId);
    }
}
