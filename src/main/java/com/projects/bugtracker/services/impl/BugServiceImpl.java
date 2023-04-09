package com.projects.bugtracker.services.impl;

import com.projects.bugtracker.entities.User;
import com.projects.bugtracker.exceptions.ResourceNotFoundException;
import com.projects.bugtracker.dto.BugDto;
import com.projects.bugtracker.dto.BugMapper;
import com.projects.bugtracker.repositories.BugRepository;
import com.projects.bugtracker.services.BugService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BugServiceImpl implements BugService {

    private final BugRepository bugRepo;

    @Override
    public List<BugDto> findAllBugs() {
        return bugRepo.findAll().stream().map(BugMapper::toDto).toList();
    }

    @Override
    public List<BugDto> findAllBugsByProject(Long projectId) {
        return null;
    }

    @Override
    public void createBug(BugDto bugDto, User user) {
        bugRepo.save(BugMapper.toBug(bugDto));
    }

    @Override
    public void updateBug(BugDto bugDto) {
        bugRepo.save(BugMapper.toBug(bugDto));
    }

    @Override
    public BugDto findBugById(Long bugId) {
        return BugMapper.toDto(bugRepo.findById(bugId).
                orElseThrow(() -> new ResourceNotFoundException("Bug", "id", bugId)));
    }

    @Override
    public void deleteBugById(Long bugId) {
        bugRepo.deleteById(bugId);
    }
}
