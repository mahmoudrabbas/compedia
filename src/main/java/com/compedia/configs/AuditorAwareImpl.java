package com.compedia.configs;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    @Override
    public Optional<String> getCurrentAuditor() {
        if(authentication == null || authentication.getPrincipal().equals("anonymousUser") || !authentication.isAuthenticated()){
            return Optional.of("system");
        }
        return Optional.of(authentication.getName());
    }
}
