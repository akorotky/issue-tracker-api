package com.projects.bugtracker.services;

import com.projects.bugtracker.dto.BugDto;
import com.projects.bugtracker.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BugService {

    BugDto findBugById(Long id);

    Page<BugDto> findAllBugs(Pageable pageable);

    List<BugDto> findAllBugsByProject(Long projectId);

    void createBug(BugDto bugDto, User user);

    void updateBug(BugDto bugDto);

    void deleteBugById(Long id);
}
