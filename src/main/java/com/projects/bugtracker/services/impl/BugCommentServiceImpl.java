package com.projects.bugtracker.services.impl;

import com.projects.bugtracker.dto.bugcommentdto.BugCommentRequestDto;
import com.projects.bugtracker.entities.Bug;
import com.projects.bugtracker.entities.BugComment;
import com.projects.bugtracker.entities.User;
import com.projects.bugtracker.exceptions.ResourceNotFoundException;
import com.projects.bugtracker.dto.bugcommentdto.BugCommentDtoMapper;
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
    private final BugCommentDtoMapper bugCommentDtoMapper;

    @Override
    public Page<BugComment> findAllBugComments(Pageable pageable) {
        return bugCommentRepository.findAll(pageable);
    }

    @Override
    public BugComment findBugCommentById(Long commentId) {
        return bugCommentRepository.findById(commentId).
                orElseThrow(() -> new ResourceNotFoundException("Bug comment", "id", commentId));
    }

    @Override
    public Page<BugComment> findAllCommentsByBugId(Long bugId, Pageable pageable) {
        Bug bug = bugRepository.findById(bugId)
                .orElseThrow(() -> new ResourceNotFoundException("Bug", "id", bugId));
        return bugCommentRepository.findByBug(bug, pageable);
    }

    @Override
    public void createBugComment(BugCommentRequestDto bugCommentRequestDto, User user) {
        Bug bug = bugRepository.findById(bugCommentRequestDto.bugId()).
                orElseThrow(() -> new ResourceNotFoundException("Bug", "id", bugCommentRequestDto.bugId()));
        BugComment bugComment = bugCommentDtoMapper.toEntity(bugCommentRequestDto);
        bugComment.setAuthor(user);
        bugComment.setBug(bug);
        bugCommentRepository.save(bugComment);
    }

    @Override
    public void updateBugComment(BugCommentRequestDto bugCommentRequestDto) {
        bugCommentRepository.save(bugCommentDtoMapper.toEntity(bugCommentRequestDto));
    }

    @Override
    public void deleteBugCommentById(Long commentId) {
        bugCommentRepository.deleteById(commentId);
    }
}
