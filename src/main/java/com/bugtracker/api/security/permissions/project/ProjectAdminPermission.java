package com.bugtracker.api.security.permissions.project;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasPermission(#projectId,'com.bugtracker.api.entities.Project', 'ADMINISTRATION')")
public @interface ProjectAdminPermission {
}
