package ru.virgil.example.util.security.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import ru.virgil.example.security.UserAuthority;
import ru.virgil.example.util.security.MockAuthorizationService;
import ru.virgil.security.mock.MockSecurityContextFactory;

import java.util.Set;

@Component
public class MockUserSecurityContextFactory extends MockSecurityContextFactory<WithMockFirebaseUser> {

    public MockUserSecurityContextFactory(MockAuthorizationService mockAuthorizationService) {
        super(mockAuthorizationService);
    }

    @Override
    public SecurityContext createSecurityContext(WithMockFirebaseUser annotation) {
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(UserAuthority.ROLE_USER.name()));
        return authenticate(annotation.firebaseUserId(), annotation.firebaseAuthToken(), authorities);
    }
}
