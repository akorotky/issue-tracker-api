package com.bugtracker.api.security.acl;

import org.springframework.security.acls.model.Permission;

public interface AclPermissionService {

    void grantPermissions(Object object, Long objectId, String username, Permission permission);
}
