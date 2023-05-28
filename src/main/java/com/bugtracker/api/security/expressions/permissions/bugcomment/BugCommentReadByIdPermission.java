package com.bugtracker.api.security.expressions.permissions.bugcomment;

import org.springframework.security.access.prepost.PostAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PostAuthorize("!returnObject.bug.project.isPrivate || hasPermission(#returnObject.bug.project, 'READ') || hasRole('ADMIN')")
public @interface BugCommentReadByIdPermission {
}
