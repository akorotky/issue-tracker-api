package com.projects.bugtracker.services;

import com.projects.bugtracker.models.User;
import com.projects.bugtracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(Long userId) {
        return repository.getReferenceById(userId);
    }

    @Override
    public void createOrUpdate(User user) {
        repository.save(user);
    }

    @Override
    public void deleteById(Long userId) {
        repository.deleteById(userId);
    }
}
