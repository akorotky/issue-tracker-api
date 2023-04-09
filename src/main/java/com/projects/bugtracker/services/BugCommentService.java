package com.projects.bugtracker.services;

import com.projects.bugtracker.dto.BugCommentDto;
import com.projects.bugtracker.dto.UserDto;

import java.util.List;

public interface BugCommentService {

    List<BugCommentDto> findAllBugComments();

    BugCommentDto findBugCommentById(Long id);

    void createBugComment(BugCommentDto bugCommentDto, UserDto userDto);

    void updateBugComment(BugCommentDto bugCommentDto);

    void deleteBugCommentById(Long id);
}
