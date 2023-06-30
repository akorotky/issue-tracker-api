package com.akorotky.issuetrackerapi.security.expressions.permissions.comment;

import org.springframework.security.access.prepost.PostAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PostAuthorize("!returnObject.issue.project.isPrivate || hasPermission(returnObject.issue.project, 'READ') || hasRole('ADMIN')")
public @interface CommentReadByIdPermission {
}
