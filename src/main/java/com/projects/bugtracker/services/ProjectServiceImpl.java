package com.projects.bugtracker.services;

import com.projects.bugtracker.models.Project;
import com.projects.bugtracker.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository repository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Project> findAll() {
        return repository.findAll();
    }

    @Override
    public Project findById(Long projectId) {
        return repository.getReferenceById(projectId);
    }

    @Override
    public void createOrUpdate(Project project) {
        repository.save(project);
    }

    @Override
    public void deleteById(Long projectId) {
        repository.deleteById(projectId);
    }

}
