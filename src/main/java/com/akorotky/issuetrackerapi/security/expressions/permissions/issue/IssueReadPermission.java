package com.akorotky.issuetrackerapi.security.expressions.permissions.issue;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("!#issue.project.isPrivate || hasPermission(#issue, 'READ') || hasRole('ADMIN')")
public @interface IssueReadPermission {
}
