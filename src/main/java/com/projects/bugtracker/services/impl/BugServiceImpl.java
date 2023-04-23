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

@Service
@RequiredArgsConstructor
public class BugServiceImpl implements BugService {

    private final BugRepository bugRepository;
    private final ProjectRepository projectRepository;

    @Override
    public Page<BugDto> findAllBugs(Pageable pageable) {
        return bugRepository.findAll(pageable).map(BugMapper::toDto);
    }

    @Override
    public Page<BugDto> findAllBugsByProjectId(Long projectId, Pageable pageable) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
        return bugRepository.findByProject(project, pageable).map(BugMapper::toDto);
    }

    @Override
    public void createBug(BugDto bugDto, User user) {
        Project project = projectRepository.findById(bugDto.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", bugDto.projectId()));
        Bug bug = BugMapper.toBug(bugDto);
        bug.setProject(project);
        bug.setAuthor(user);
        bugRepository.save(bug);
    }

    @Override
    public void updateBug(BugDto bugDto) {
        bugRepository.save(BugMapper.toBug(bugDto));
    }

    @Override
    public BugDto findBugById(Long bugId) {
        return BugMapper.toDto(bugRepository.findById(bugId).
                orElseThrow(() -> new ResourceNotFoundException("Bug", "id", bugId)));
    }

    @Override
    public void deleteBugById(Long bugId) {
        bugRepository.deleteById(bugId);
    }
}
