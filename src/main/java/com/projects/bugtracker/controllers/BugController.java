package com.projects.bugtracker.controllers;

import com.projects.bugtracker.assemblers.BugModelAssembler;
import com.projects.bugtracker.dto.BugDto;
import com.projects.bugtracker.security.CurrentUser;
import com.projects.bugtracker.security.UserPrincipal;
import com.projects.bugtracker.services.BugService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/bugs")
public class BugController {

    private final BugService bugService;
    private final BugModelAssembler bugModelAssembler;
    private final PagedResourcesAssembler<BugDto> pagedResourcesAssembler;

    @GetMapping
    public CollectionModel<EntityModel<BugDto>> getBugsPage(@PageableDefault(page = 0, size = 15) Pageable pageable) {
        Page<BugDto> bugsPage = bugService.findAllBugs(pageable);
        return pagedResourcesAssembler.toModel(bugsPage, bugModelAssembler);

    }

    @GetMapping("{bugId}")
    public EntityModel<BugDto> getBug(@PathVariable Long bugId) {
        BugDto bug = bugService.findBugById(bugId);
        return bugModelAssembler.toModel(bug);
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
