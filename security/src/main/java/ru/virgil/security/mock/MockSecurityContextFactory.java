package ru.virgil.security.mock;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import ru.virgil.security.InternalSecurityToken;
import ru.virgil.security.entity.SecurityUser;
import ru.virgil.security.firebase.FirebaseAuthenticationToken;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class MockSecurityContextFactory<A extends Annotation> implements WithSecurityContextFactory<A> {

    private final MockAuthorizationService mockAuthorizationService;

    /**
     * @param userId      if this value is empty string then it will be replaced by random value
     * @param authToken   if this value is empty string then it will be replaced by random value
     * @param authorities collection of required authorities
     */
    public SecurityContext authenticate(String userId, String authToken, Collection<GrantedAuthority> authorities) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        userId = userId.isEmpty() ? UUID.randomUUID().toString() : authToken;
        authToken = authToken.isEmpty() ? UUID.randomUUID().toString() : authToken;
        FirebaseAuthenticationToken token = new FirebaseAuthenticationToken(authorities, userId, authToken);
        SecurityUser userDetails = mockAuthorizationService.authOrRegister(token);
        InternalSecurityToken internalSecurityToken = new InternalSecurityToken(token.getAuthorities(), userDetails);
        securityContext.setAuthentication(internalSecurityToken);
        return securityContext;
    }
}
