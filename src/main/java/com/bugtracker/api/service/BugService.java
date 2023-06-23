package com.bugtracker.api.service;

import com.bugtracker.api.entity.Project;
import com.bugtracker.api.entity.User;
import com.bugtracker.api.dto.bug.BugRequestDto;
import com.bugtracker.api.entity.Bug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BugService {

    Bug findBugById(Long bugId);

    Page<Bug> findAllBugs(Pageable pageable);

    Page<Bug> findAllBugsByProject(Project project, Pageable pageable);

    Bug createBug(Project project, BugRequestDto bugRequestDto, User user);

    void updateBug(Bug bug, BugRequestDto bugRequestDto);

    void deleteBug(Bug bug);
}
