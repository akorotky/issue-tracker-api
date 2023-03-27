package com.projects.bugtracker.services;

import com.projects.bugtracker.models.BugComment;

import java.util.List;

public interface BugCommentService {
    List<BugComment> findAll();

    BugComment findById(Long id);

    void createOrUpdate(BugComment bug);

    void deleteById(Long id);
}
