package com.bugtracker.api.security.expressions.permissions.bugcomment;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasPermission(#bugComment, 'ADMINISTRATION') || hasRole('ADMIN')")
public @interface BugCommentAuthorPermission {
}
