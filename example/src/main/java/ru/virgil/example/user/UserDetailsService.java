package ru.virgil.example.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.virgil.example.security.SecurityUserDetails;
import ru.virgil.example.system.SimpleJpaAccess;
import ru.virgil.security.InternalSecurityToken;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements SimpleJpaAccess<UserDetails, UUID> {

    private final UserDetailsRepository repository;

    public UserDetails getCurrentUser() {
        InternalSecurityToken token = (InternalSecurityToken) SecurityContextHolder.getContext().getAuthentication();
        SecurityUserDetails securityUserDetails = (SecurityUserDetails) token.getPrincipal();
        return repository.findBySecurityUserDetails(securityUserDetails).orElseThrow();
    }

    @Override
    public JpaRepository<UserDetails, UUID> getRepository() {
        return repository;
    }

}
