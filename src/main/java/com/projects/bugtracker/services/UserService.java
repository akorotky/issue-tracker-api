package com.projects.bugtracker.services;

import com.projects.bugtracker.dto.ProjectDto;
import com.projects.bugtracker.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserDto findUserById(Long id);

    UserDto findUserByUsername(String username);

    List<ProjectDto> getOwnedProjects(String username);

    List<ProjectDto> getSharedProjects(String username);

    Page<UserDto> findAllUsers(Pageable pageable);

    void createUser(UserDto userDto);

    void updateUser(UserDto userDto);

    void deleteUserById(Long id);

    void deleteUserByUsername(String username);
}
