package com.bugtracker.api.security.data.audit;

import com.bugtracker.api.security.principal.UserPrincipal;
import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

class SecurityAuditorAware implements AuditorAware<Long> {

    @Override
    public @NonNull Optional<Long> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(UserPrincipal.class::cast)
                .map(UserPrincipal::getId);
    }
}


