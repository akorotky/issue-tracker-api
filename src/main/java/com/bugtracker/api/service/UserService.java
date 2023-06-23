package com.bugtracker.api.service;

import com.bugtracker.api.dto.user.UserRequestDto;
import com.bugtracker.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User findUserById(Long userId);

    User findUserByUsername(String username);

    Page<User> findAllUsers(Pageable pageable);

    void createUser(UserRequestDto userRequestDto);

    void updateUser(User user, UserRequestDto userRequestDto);

    void deleteUser(User user);
}
