package com.projects.bugtracker.services;

import com.projects.bugtracker.models.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(Long id);

    void createOrUpdate(User user);

    void deleteById(Long id);
}
