package com.bugtracker.api.services.impl;

import com.bugtracker.api.dto.bugcommentdto.BugCommentDtoMapper;
import com.bugtracker.api.dto.bugcommentdto.BugCommentRequestDto;
import com.bugtracker.api.entities.BugComment;
import com.bugtracker.api.entities.User;
import com.bugtracker.api.exceptions.ResourceNotFoundException;
import com.bugtracker.api.repositories.BugCommentRepository;
import com.bugtracker.api.repositories.BugRepository;
import com.bugtracker.api.services.BugCommentService;
import com.bugtracker.api.entities.Bug;
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
