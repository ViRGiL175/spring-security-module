package ru.virgil.example.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.virgil.example.security.SecurityUserDetails;
import ru.virgil.security.InternalSecurityToken;

@Service
@RequiredArgsConstructor
public class UserDetailsService {

    @Getter
    private final UserDetailsRepository repository;

    public UserDetails getCurrentUser() {
        InternalSecurityToken token = (InternalSecurityToken) SecurityContextHolder.getContext().getAuthentication();
        SecurityUserDetails securityUserDetails = (SecurityUserDetails) token.getPrincipal();
        return repository.findBySecurityUserDetails(securityUserDetails).orElseThrow();
    }

}
