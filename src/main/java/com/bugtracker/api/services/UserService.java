package com.bugtracker.api.services;

import com.bugtracker.api.dto.userdto.UserRequestDto;
import com.bugtracker.api.entities.User;
import com.bugtracker.api.entities.Project;
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
