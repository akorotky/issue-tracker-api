package com.projects.bugtracker.services;

import com.projects.bugtracker.dto.bugdto.BugRequestDto;
import com.projects.bugtracker.entities.Bug;
import com.projects.bugtracker.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BugService {

    Bug findBugById(Long id);

    Page<Bug> findAllBugs(Pageable pageable);

    Page<Bug> findAllBugsByProjectId(Long projectId, Pageable pageable);

    void createBug(BugRequestDto bugRequestDto, User user);

    void updateBug(BugRequestDto bugRequestDto);

    void deleteBugById(Long id);
}
