package com.akorotky.issuetrackerapi.security.expressions.permissions.bug;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("!#bug.project.isPrivate || hasPermission(#bug, 'READ') || hasRole('ADMIN')")
public @interface BugReadPermission {
}
