package com.bugtracker.api.security.expressions.permissions.bugcomment;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("#bug.project != null? hasPermission(#bug.project, 'READ') || hasRole('ADMIN') : true")
@PostAuthorize("returnObject != null? !returnObject.bug.project.isPrivate || hasPermission(#returnObject.bug.project, 'READ') || hasRole('ADMIN') : true")
public @interface BugCommentReadPermission {
}
