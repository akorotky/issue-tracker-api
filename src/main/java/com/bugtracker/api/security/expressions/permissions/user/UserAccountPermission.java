package com.bugtracker.api.security.expressions.permissions.user;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("(#user == principal.user) || (#username == principal.username) || (#userId == principal.user.id) || hasRole('ADMIN')")
public @interface UserAccountPermission {
}
