package com.redditclone.security;

import com.redditclone.entity.User;
import com.redditclone.exception.ResourceNotFoundException;
import com.redditclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Resolves the currently authenticated User entity (if any) from the
 * Spring Security context set up by JwtAuthFilter.
 */
@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final UserRepository userRepository;

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    public User requireCurrentUser() {
        return getCurrentUser()
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }
}
