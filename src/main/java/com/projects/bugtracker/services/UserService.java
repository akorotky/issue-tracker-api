package com.projects.bugtracker.services;

import com.projects.bugtracker.dto.userdto.UserRequestDto;
import com.projects.bugtracker.entities.Project;
import com.projects.bugtracker.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    User findUserById(Long id);

    User findUserByUsername(String username);

    List<Project> getOwnedProjects(String username);

    List<Project> getSharedProjects(String username);

    Page<User> findAllUsers(Pageable pageable);

    void createUser(UserRequestDto userRequestDto);

    void updateUser(UserRequestDto userRequestDto);

    void deleteUserById(Long id);

    void deleteUserByUsername(String username);
}
