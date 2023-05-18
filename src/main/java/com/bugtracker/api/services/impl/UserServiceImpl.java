package com.bugtracker.api.services.impl;

import com.bugtracker.api.dto.userdto.UserDtoMapper;
import com.bugtracker.api.dto.userdto.UserRequestDto;
import com.bugtracker.api.entities.Project;
import com.bugtracker.api.entities.role.Role;
import com.bugtracker.api.entities.User;
import com.bugtracker.api.entities.role.RoleType;
import com.bugtracker.api.exceptions.ResourceNotFoundException;
import com.bugtracker.api.repositories.RoleRepository;
import com.bugtracker.api.repositories.UserRepository;
import com.bugtracker.api.services.UserService;
import com.bugtracker.api.entities.Bug;
import com.bugtracker.api.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final String USER_NOT_FOUND_MSG = "User with %s %s not found";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDtoMapper userDtoMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, "username", username)));
        return new UserPrincipal(user);
    }

    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    @Override
    public User createUser(UserRequestDto userRequestDto) {
        User user = userDtoMapper.toEntity(userRequestDto);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findRoleByName(RoleType.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleType.USER)));

        user.setRoles(roles);
        user.setAccountEnabled(true);
        user.setAccountLocked(false);
        user.setAccountExpired(false);
        user.setCredentialsExpired(false);

        userRepository.saveAndFlush(user);
        return user;
    }

    @Override
    public void updateUser(UserRequestDto userRequestDto) {
        userRepository.save(userDtoMapper.toEntity(userRequestDto));
    }

    @Override
    public void deleteUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        for (Project project : user.getSharedProjects()) {
            project.removeCollaborator(user);
        }

        userRepository.deleteById(userId);
    }

    @Override
    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        for (Project project : user.getSharedProjects()) {
            project.removeCollaborator(user);
        }

        for (Bug bug : user.getBugs()) {
            bug.setAuthor(null);
        }

        userRepository.deleteByUsername(username);
    }

    @Override
    public List<Project> getOwnedProjects(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return user.getOwnedProjects().stream().toList();
    }

    @Override
    public List<Project> getSharedProjects(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return user.getSharedProjects().stream().toList();
    }
}
