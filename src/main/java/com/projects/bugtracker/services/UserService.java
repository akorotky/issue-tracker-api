package com.projects.bugtracker.services;

import com.projects.bugtracker.dto.ProjectDto;
import com.projects.bugtracker.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findUserById(Long id);

    UserDto findUserByUsername(String username);

    List<ProjectDto> getOwnedProjects(String username);

    List<ProjectDto> getSharedProjects(String username);

    List<UserDto> findAllUsers();

    void createUser(UserDto userDto);

    void updateUser(UserDto userDto);

    void deleteUserById(Long id);

    void deleteUserByUsername(String username);
}
