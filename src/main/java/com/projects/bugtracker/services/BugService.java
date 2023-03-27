package com.projects.bugtracker.services;

import com.projects.bugtracker.models.Bug;

import java.util.List;

public interface BugService {

    List<Bug> findAll();

    Bug findById(Long id);

    void createOrUpdate(Bug bug);

    void deleteById(Long id);
}
