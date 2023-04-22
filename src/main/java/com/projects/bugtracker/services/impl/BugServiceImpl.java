package com.projects.bugtracker.services.impl;

import com.projects.bugtracker.entities.Bug;
import com.projects.bugtracker.entities.Project;
import com.projects.bugtracker.entities.User;
import com.projects.bugtracker.exceptions.ResourceNotFoundException;
import com.projects.bugtracker.dto.BugDto;
import com.projects.bugtracker.dto.BugMapper;
import com.projects.bugtracker.repositories.BugRepository;
import com.projects.bugtracker.repositories.ProjectRepository;
import com.projects.bugtracker.services.BugService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BugServiceImpl implements BugService {

    private final BugRepository bugRepo;
    private final ProjectRepository projectRepo;

    @Override
    public Page<BugDto> findAllBugs(Pageable pageable) {
        return bugRepo.findAll(pageable).map(BugMapper::toDto);
    }

    @Override
    public List<BugDto> findAllBugsByProject(Long projectId) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
        return bugRepo.findAllByProject(project).stream().map(BugMapper::toDto).toList();
    }

    @Override
    public void createBug(BugDto bugDto, User user) {
        Project project = projectRepo.findById(bugDto.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", bugDto.projectId()));
        Bug bug = BugMapper.toBug(bugDto);
        bug.setProject(project);
        bug.setAuthor(user);
        bugRepo.save(bug);
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
