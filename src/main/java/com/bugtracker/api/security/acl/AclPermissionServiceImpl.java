package com.bugtracker.api.security.acl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AclPermissionServiceImpl implements AclPermissionService {

    private final MutableAclService mutableAclService;

    @Transactional
    @Override
    public void grantPermissions(Object object, Long objectId, String username, Permission permission) {
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(object.getClass(), objectId);
        MutableAcl acl;
        try {
            acl = (MutableAcl) mutableAclService.readAclById(objectIdentity);
        } catch (NotFoundException e) {
            acl = mutableAclService.createAcl(objectIdentity);
        }
        acl.insertAce(acl.getEntries().size(), permission, new PrincipalSid(username), true);
        mutableAclService.updateAcl(acl);
    }
}
