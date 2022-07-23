package ru.virgil.example.util.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;
import ru.virgil.example.security.SecurityUserDetails;
import ru.virgil.example.security.SecurityUserDetailsService;
import ru.virgil.example.security.UserAuthority;
import ru.virgil.security.InternalSecurityToken;
import ru.virgil.security.firebase.FirebaseAuthenticationToken;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class TestSecurityContextFactoryUser implements WithSecurityContextFactory<WithMockFirebaseUser> {

    private final SecurityUserDetailsService securityUserDetailsService;

    @Override
    public SecurityContext createSecurityContext(WithMockFirebaseUser mockFirebaseUser) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        FirebaseAuthenticationToken firebaseAuthenticationToken = new FirebaseAuthenticationToken(
                Set.of(new SimpleGrantedAuthority(UserAuthority.ROLE_USER.name())),
                mockFirebaseUser.firebaseUserId(),
                mockFirebaseUser.firebaseAuthToken()
        );
        SecurityUserDetails securityUserDetails = securityUserDetailsService.register(firebaseAuthenticationToken);
        InternalSecurityToken internalSecurityToken = new InternalSecurityToken(
                firebaseAuthenticationToken.getAuthorities(), securityUserDetails);
        securityContext.setAuthentication(internalSecurityToken);
        return securityContext;
    }
}
