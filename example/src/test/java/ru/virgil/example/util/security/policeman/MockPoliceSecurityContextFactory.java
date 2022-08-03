package ru.virgil.example.util.security.policeman;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import ru.virgil.example.security.UserAuthority;
import ru.virgil.example.util.security.MockAuthorizationService;
import ru.virgil.security.mock.MockSecurityContextFactory;

import java.util.Set;

@Component
public class MockPoliceSecurityContextFactory extends MockSecurityContextFactory<WithMockFirebasePoliceman> {

    public MockPoliceSecurityContextFactory(MockAuthorizationService mockAuthorizationService) {
        super(mockAuthorizationService);
    }

    @Override
    public SecurityContext createSecurityContext(WithMockFirebasePoliceman annotation) {
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(UserAuthority.ROLE_POLICE.name()));
        return authenticate(annotation.firebaseUserId(), annotation.firebaseAuthToken(), authorities);
    }
}
