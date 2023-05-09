package com.bugtracker.api.services;

import com.bugtracker.api.entities.User;
import com.bugtracker.api.dto.bugdto.BugRequestDto;
import com.bugtracker.api.entities.Bug;
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
