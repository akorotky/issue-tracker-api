package com.bugtracker.api.services.impl;

import com.bugtracker.api.dto.bug.BugDtoMapper;
import com.bugtracker.api.dto.bug.BugRequestDto;
import com.bugtracker.api.entities.Project;
import com.bugtracker.api.entities.User;
import com.bugtracker.api.exceptions.ResourceNotFoundException;
import com.bugtracker.api.repositories.BugRepository;
import com.bugtracker.api.security.acl.AclPermissionService;
import com.bugtracker.api.security.expressions.permissions.bug.BugAuthorPermission;
import com.bugtracker.api.security.expressions.permissions.bug.BugCreatePermission;
import com.bugtracker.api.security.expressions.permissions.bug.BugReadByIdPermission;
import com.bugtracker.api.security.expressions.permissions.project.ProjectReadPermission;
import com.bugtracker.api.security.expressions.permissions.role.IsUser;
import com.bugtracker.api.services.BugService;
import com.bugtracker.api.entities.Bug;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BugServiceImpl implements BugService {

    private final BugRepository bugRepository;
    private final BugDtoMapper bugDtoMapper;
    private final AclPermissionService aclPermissionService;
    private final Permission[] bugAuthorPermissions = {BasePermission.ADMINISTRATION};

    @IsUser
    @Override
    public Page<Bug> findAllBugs(Pageable pageable) {
        return bugRepository.findAll(pageable);
    }

    @ProjectReadPermission
    @Override
    public Page<Bug> findAllBugsByProject(Project project, Pageable pageable) {
        return bugRepository.findByProject(project, pageable);
    }

    @IsUser
    @BugReadByIdPermission
    @Override
    public Bug findBugById(Long bugId) {
        return bugRepository.findById(bugId).
                orElseThrow(() -> new ResourceNotFoundException("Bug", "id", bugId));
    }

    @BugCreatePermission
    @Override
    public void createBug(Project project, BugRequestDto bugRequestDto, User user) {
        Bug bug = bugDtoMapper.toEntity(bugRequestDto);
        bug.setProject(project);
        bug.setAuthor(user);
        bugRepository.saveAndFlush(bug);
        aclPermissionService.grantPermissions(bug, bug.getId(), user.getUsername(), bugAuthorPermissions);

    }

    @BugAuthorPermission
    @Override
    public void updateBug(Bug bug, BugRequestDto bugRequestDto) {
        bugRepository.save(bugDtoMapper.toEntity(bugRequestDto));
    }

    @BugAuthorPermission
    @Override
    public void deleteBug(Bug bug) {
        bugRepository.deleteById(bug.getId());
    }
}
