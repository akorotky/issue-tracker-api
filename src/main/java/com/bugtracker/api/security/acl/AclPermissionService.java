package com.bugtracker.api.security.acl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AclPermissionService {

    private final MutableAclService mutableAclService;

    public void grantPermissions(Object object, Long objectId, String username, Permission... permissions) {
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(object.getClass(), objectId);
        MutableAcl acl;
        try {
            acl = (MutableAcl) mutableAclService.readAclById(objectIdentity);
        } catch (NotFoundException e) {
            acl = mutableAclService.createAcl(objectIdentity);
        }
        for (Permission permission : permissions) {
            acl.insertAce(acl.getEntries().size(), permission, new PrincipalSid(username), true);
        }
        mutableAclService.updateAcl(acl);
    }

    public void revokePermissions(Object object, Long objectId, String username, Permission... permissions) {
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(object.getClass(), objectId);
        MutableAcl acl;
        try {
            acl = (MutableAcl) mutableAclService.readAclById(objectIdentity);
        } catch (NotFoundException e) {
            return;
        }
        Set<Permission> permissionSet = Arrays.stream(permissions).collect(Collectors.toSet());
        List<AccessControlEntry> aceList = new ArrayList<>(acl.getEntries());
        for (AccessControlEntry ace : aceList) {
            PrincipalSid principalSid = (PrincipalSid) ace.getSid();
            String principal = principalSid.getPrincipal();
            if (principal.equals(username) && permissionSet.contains(ace.getPermission()))
                acl.deleteAce((Integer) ace.getId());
        }
        mutableAclService.updateAcl(acl);
    }
}
