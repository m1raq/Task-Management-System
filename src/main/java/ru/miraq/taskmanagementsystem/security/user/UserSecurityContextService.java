package ru.miraq.taskmanagementsystem.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.miraq.taskmanagementsystem.entity.user.UserEntity;
import ru.miraq.taskmanagementsystem.repository.UserRepository;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserSecurityContextService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionRegistry sessionRegistry;

    @Transactional(readOnly = true)
    public UserEntity getCurrentUser() {
        return Optional
                .ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName)
                .flatMap(userRepository::findByEmail)
                .orElse(null);
    }

    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean matches(String oldPassword, String password) {
        return passwordEncoder.matches(oldPassword, password);
    }

    public void logoutUser() {
        final List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        for (final Object principal : allPrincipals) {
            if (principal instanceof org.springframework.security.core.userdetails.User springUserDetails) {
                if (springUserDetails.getUsername().equals(getCurrentUser().getEmail())) {
                    for (SessionInformation sessionInformation : sessionRegistry.getAllSessions(principal, true)) {
                        sessionInformation.expireNow();
                    }
                }
            }
        }
    }

}
