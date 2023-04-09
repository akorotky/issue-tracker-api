package com.projects.bugtracker.services.impl;

import com.projects.bugtracker.exceptions.ResourceNotFoundException;
import com.projects.bugtracker.dto.BugCommentDto;
import com.projects.bugtracker.dto.BugCommentMapper;
import com.projects.bugtracker.dto.UserDto;
import com.projects.bugtracker.repositories.BugCommentRepository;
import com.projects.bugtracker.services.BugCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BugCommentServiceImpl implements BugCommentService {
    private final BugCommentRepository bugCommentRepo;

    @Override
    public List<BugCommentDto> findAllBugComments() {
        return bugCommentRepo.findAll().stream().map(BugCommentMapper::toDto).toList();
    }

    @Override
    public BugCommentDto findBugCommentById(Long commentId) {
        return BugCommentMapper.toDto(bugCommentRepo.findById(commentId).
                orElseThrow(() -> new ResourceNotFoundException("Bug comment", "id", commentId)));
    }

    @Override
    public void createBugComment(BugCommentDto bugCommentDto, UserDto userDto) {
        bugCommentRepo.save(BugCommentMapper.toBugComment(bugCommentDto));
    }

    @Override
    public void updateBugComment(BugCommentDto bugCommentDto) {
        bugCommentRepo.save(BugCommentMapper.toBugComment(bugCommentDto));
    }

    @Override
    public void deleteBugCommentById(Long commentId) {
        bugCommentRepo.deleteById(commentId);
    }
}
