package com.projects.bugtracker.services;

import com.projects.bugtracker.models.Project;

import java.util.List;

public interface ProjectService {

    List<Project> findAll();

    Project findById(Long id);

    void createOrUpdate(Project project);

    void deleteById(Long id);

}
