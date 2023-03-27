package com.projects.bugtracker.services;

import com.projects.bugtracker.models.Bug;
import com.projects.bugtracker.repositories.BugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BugServiceImpl implements BugService {

    private BugRepository repository;

    @Autowired
    public BugServiceImpl(BugRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Bug> findAll() {
        return repository.findAll();
    }

    @Override
    public Bug findById(Long bugId) {
        return repository.getReferenceById(bugId);
    }

    @Override
    public void createOrUpdate(Bug bug) {
        repository.save(bug);
    }

    @Override
    public void deleteById(Long bugId) {
        repository.deleteById(bugId);
    }
}
