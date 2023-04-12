package com.projects.bugtracker.controllers;

import com.projects.bugtracker.dto.BugDto;
import com.projects.bugtracker.security.CurrentUser;
import com.projects.bugtracker.security.UserPrincipal;
import com.projects.bugtracker.services.BugService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("api/bugs")
public class BugController {
    private final BugService bugService;
    @GetMapping
    public List<BugDto> getAllBugs(@RequestParam Long projectId) {

        return bugService.findAllBugs();
    }

    @GetMapping("{bugId}")
    public BugDto getBug(@PathVariable Long bugId) {
        return bugService.findBugById(bugId);
    }

    @PostMapping
    public void createBug(@Valid @RequestBody BugDto bugDto, @CurrentUser UserPrincipal currentUser) {
        bugService.createBug(bugDto, currentUser.getUser());
    }
}
