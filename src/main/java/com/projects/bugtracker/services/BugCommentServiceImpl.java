package com.projects.bugtracker.services;

import com.projects.bugtracker.models.BugComment;
import com.projects.bugtracker.repositories.BugCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BugCommentServiceImpl implements BugCommentService {
    private BugCommentRepository repository;

    @Autowired
    public BugCommentServiceImpl(BugCommentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BugComment> findAll() {
        return repository.findAll();
    }

    @Override
    public BugComment findById(Long commentId) {
        return repository.getReferenceById(commentId);
    }

    @Override
    public void createOrUpdate(BugComment comment) {
        repository.save(comment);
    }

    @Override
    public void deleteById(Long commentId) {
        repository.deleteById(commentId);
    }
}
