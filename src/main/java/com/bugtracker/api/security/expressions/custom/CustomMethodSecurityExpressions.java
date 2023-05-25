package com.bugtracker.api.security.expressions.custom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration("authorizer")
public class CustomMethodSecurityExpressions {

    @Bean
    public boolean isUserAnonymous() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }
}