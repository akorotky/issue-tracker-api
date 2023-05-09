package com.bugtracker.api.services.impl;

import com.bugtracker.api.dto.bugdto.BugDtoMapper;
import com.bugtracker.api.dto.bugdto.BugRequestDto;
import com.bugtracker.api.entities.Project;
import com.bugtracker.api.entities.User;
import com.bugtracker.api.exceptions.ResourceNotFoundException;
import com.bugtracker.api.repositories.BugRepository;
import com.bugtracker.api.services.BugService;
import com.bugtracker.api.entities.Bug;
import com.bugtracker.api.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BugServiceImpl implements BugService {

    private final BugRepository bugRepository;
    private final ProjectRepository projectRepository;
    private final BugDtoMapper bugDtoMapper;

    @Override
    public Page<Bug> findAllBugs(Pageable pageable) {
        return bugRepository.findAll(pageable);
    }

    @Override
    public Page<Bug> findAllBugsByProjectId(Long projectId, Pageable pageable) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
        return bugRepository.findByProject(project, pageable);
    }

    @Override
    public void createBug(BugRequestDto bugRequestDto, User user) {
        Project project = projectRepository.findById(bugRequestDto.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", bugRequestDto.projectId()));
        Bug bug = bugDtoMapper.toEntity(bugRequestDto);
        bug.setProject(project);
        bug.setAuthor(user);
        bugRepository.save(bug);
    }

    @Override
    public void updateBug(BugRequestDto bugRequestDto) {
        bugRepository.save(bugDtoMapper.toEntity(bugRequestDto));
    }

    @Override
    public Bug findBugById(Long bugId) {
        return bugRepository.findById(bugId).
                orElseThrow(() -> new ResourceNotFoundException("Bug", "id", bugId));
    }

    @Override
    public void deleteBugById(Long bugId) {
        bugRepository.deleteById(bugId);
    }
}
