package com.projects.bugtracker.services;

import com.projects.bugtracker.dto.BugDto;
import com.projects.bugtracker.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BugService {

    BugDto findBugById(Long id);

    Page<BugDto> findAllBugs(Pageable pageable);

    Page<BugDto> findAllBugsByProjectId(Long projectId, Pageable pageable);

    void createBug(BugDto bugDto, User user);

    void updateBug(BugDto bugDto);

    void deleteBugById(Long id);
}
