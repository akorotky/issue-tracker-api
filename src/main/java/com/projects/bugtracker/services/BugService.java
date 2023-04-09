package com.projects.bugtracker.services;

import com.projects.bugtracker.dto.BugDto;
import com.projects.bugtracker.entities.User;

import java.util.List;

public interface BugService {

    BugDto findBugById(Long id);

    List<BugDto> findAllBugs();

    List<BugDto> findAllBugsByProject(Long projectId);

    void createBug(BugDto bugDto, User user);

    void updateBug(BugDto bugDto);

    void deleteBugById(Long id);
}
