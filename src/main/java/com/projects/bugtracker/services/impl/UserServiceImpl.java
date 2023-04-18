package com.projects.bugtracker.services.impl;

import com.projects.bugtracker.dto.ProjectDto;
import com.projects.bugtracker.dto.ProjectMapper;
import com.projects.bugtracker.entities.Bug;
import com.projects.bugtracker.entities.Project;
import com.projects.bugtracker.entities.Role;
import com.projects.bugtracker.constants.RoleType;
import com.projects.bugtracker.exceptions.ResourceNotFoundException;
import com.projects.bugtracker.dto.UserDto;
import com.projects.bugtracker.dto.UserMapper;
import com.projects.bugtracker.entities.User;
import com.projects.bugtracker.repositories.RoleRepository;
import com.projects.bugtracker.repositories.UserRepository;
import com.projects.bugtracker.security.UserPrincipal;
import com.projects.bugtracker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final String USER_NOT_FOUND_MSG = "User with %s %s not found";
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, "username", username)));
        return new UserPrincipal(user);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepo.findAll().stream().map(UserMapper::toDto).toList();
    }

    @Override
    public UserDto findUserByUsername(String username) {
        return UserMapper.toDto(userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username)));
    }

    @Override
    public UserDto findUserById(Long userId) {
        return UserMapper.toDto(userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId)));
    }

    @Override
    public void createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepo.findRoleByName(RoleType.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleType.USER)));

        user.setRoles(roles);
        user.setAccountEnabled(true);
        user.setAccountLocked(false);
        user.setAccountExpired(false);
        user.setCredentialsExpired(false);

        userRepo.save(user);
    }

    @Override
    public void updateUser(UserDto userDto) {
        userRepo.save(UserMapper.toUser(userDto));
    }

    @Override
    public void deleteUserById(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        for (Project project : user.getSharedProjects()) {
            project.removeCollaborator(user);
        }

        userRepo.deleteById(userId);
    }

    @Override
    public void deleteUserByUsername(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        for (Project project : user.getSharedProjects()) {
            project.removeCollaborator(user);
        }

        for (Bug bug : user.getBugs()) {
            bug.setAuthor(null);
        }

        userRepo.deleteByUsername(username);
    }

    @Override
    public List<ProjectDto> getOwnedProjects(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        return user.getOwnedProjects().stream().map(ProjectMapper::toDto).toList();
    }

    @Override
    public List<ProjectDto> getSharedProjects(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        return user.getSharedProjects().stream().map(ProjectMapper::toDto).toList();
    }
}
