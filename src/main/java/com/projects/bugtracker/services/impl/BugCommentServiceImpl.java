package com.projects.bugtracker.services.impl;

import com.projects.bugtracker.entities.Bug;
import com.projects.bugtracker.entities.BugComment;
import com.projects.bugtracker.entities.User;
import com.projects.bugtracker.exceptions.ResourceNotFoundException;
import com.projects.bugtracker.dto.BugCommentDto;
import com.projects.bugtracker.dto.BugCommentMapper;
import com.projects.bugtracker.repositories.BugCommentRepository;
import com.projects.bugtracker.repositories.BugRepository;
import com.projects.bugtracker.services.BugCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BugCommentServiceImpl implements BugCommentService {

    private final BugCommentRepository bugCommentRepository;
    private final BugRepository bugRepository;

    @Override
    public Page<BugCommentDto> findAllBugComments(Pageable pageable) {
        return bugCommentRepository.findAll(pageable).map(BugCommentMapper::toDto);
    }

    @Override
    public BugCommentDto findBugCommentById(Long commentId) {
        return BugCommentMapper.toDto(bugCommentRepository.findById(commentId).
                orElseThrow(() -> new ResourceNotFoundException("Bug comment", "id", commentId)));
    }

    @Override
    public Page<BugCommentDto> findAllCommentsByBugId(Long bugId, Pageable pageable) {
        Bug bug = bugRepository.findById(bugId)
                .orElseThrow(() -> new ResourceNotFoundException("Bug", "id", bugId));
        return bugCommentRepository.findByBug(bug, pageable).map(BugCommentMapper::toDto);
    }

    @Override
    public void createBugComment(BugCommentDto bugCommentDto, User user) {
        Bug bug = bugRepository.findById(bugCommentDto.bugId()).
                orElseThrow(() -> new ResourceNotFoundException("Bug", "id", bugCommentDto.bugId()));
        BugComment bugComment = BugCommentMapper.toBugComment(bugCommentDto);
        bugComment.setAuthor(user);
        bugComment.setBug(bug);
        bugCommentRepository.save(bugComment);
    }

    @Override
    public void updateBugComment(BugCommentDto bugCommentDto) {
        bugCommentRepository.save(BugCommentMapper.toBugComment(bugCommentDto));
    }

    @Override
    public void deleteBugCommentById(Long commentId) {
        bugCommentRepository.deleteById(commentId);
    }
}
