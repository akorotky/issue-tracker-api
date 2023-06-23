package com.bugtracker.api.service;

import com.bugtracker.api.dto.bug.BugDtoMapper;
import com.bugtracker.api.dto.bug.BugRequestDto;
import com.bugtracker.api.entity.Project;
import com.bugtracker.api.entity.User;
import com.bugtracker.api.exception.ResourceNotFoundException;
import com.bugtracker.api.repository.BugRepository;
import com.bugtracker.api.security.acl.AclPermissionService;
import com.bugtracker.api.security.expressions.permissions.bug.BugAuthorPermission;
import com.bugtracker.api.security.expressions.permissions.bug.BugCreatePermission;
import com.bugtracker.api.security.expressions.permissions.bug.BugReadByIdPermission;
import com.bugtracker.api.security.expressions.permissions.project.ProjectReadPermission;
import com.bugtracker.api.security.expressions.permissions.role.IsUser;
import com.bugtracker.api.entity.Bug;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BugService {

    private final BugRepository bugRepository;
    private final BugDtoMapper bugDtoMapper;
    private final AclPermissionService aclPermissionService;
    private final Permission[] bugAuthorPermissions = {BasePermission.ADMINISTRATION};

    @IsUser
    @Transactional(readOnly = true)
    public Page<Bug> findAllBugs(Pageable pageable) {
        Page<Bug> bugs = bugRepository.findAll(pageable);
        if (bugs.isEmpty()) throw new ResourceNotFoundException("No bugs found");
        return bugs;
    }

    @ProjectReadPermission
    @Transactional(readOnly = true)
    public Page<Bug> findAllBugsByProject(Project project, Pageable pageable) {
        Page<Bug> bugs = bugRepository.findByProject(project, pageable);
        if (bugs.isEmpty())
            throw new ResourceNotFoundException("No bugs for project with id=" + project.getId() + " found");
        return bugs;
    }

    @IsUser
    @BugReadByIdPermission
    @Transactional(readOnly = true)
    public Bug findBugById(Long bugId) {
        return bugRepository.findById(bugId).
                orElseThrow(() -> new ResourceNotFoundException("Bug with id=" + bugId + " not found"));
    }

    @BugCreatePermission
    public Bug createBug(Project project, BugRequestDto bugRequestDto, User user) {
        Bug bug = bugDtoMapper.toEntity(bugRequestDto);
        bug.setProject(project);
        bug.setAuthor(user);
        bugRepository.saveAndFlush(bug);
        aclPermissionService.grantPermissions(bug, bug.getId(), user.getUsername(), bugAuthorPermissions);
        return bug;
    }

    @BugAuthorPermission
    public void updateBug(Bug bug, BugRequestDto bugRequestDto) {
        bugRepository.save(bugDtoMapper.toEntity(bugRequestDto));
    }

    @BugAuthorPermission
    public void deleteBug(Bug bug) {
        bugRepository.deleteById(bug.getId());
    }
}
