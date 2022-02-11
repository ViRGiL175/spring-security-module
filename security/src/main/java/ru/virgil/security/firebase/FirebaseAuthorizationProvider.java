package ru.virgil.security.firebase;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.virgil.security.InternalSecurityToken;
import ru.virgil.security.entity.SecurityUser;
import ru.virgil.security.service.SecurityUserService;

import static com.google.common.truth.Truth.assertThat;

@RequiredArgsConstructor
@Component
public class FirebaseAuthorizationProvider implements AuthenticationProvider {

    private final FirebaseService firebaseService;
    private final SecurityUserService securityUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        FirebaseAuthenticationToken firebaseAuthenticationToken = (FirebaseAuthenticationToken) authentication;
        FirebaseToken firebaseToken;
        try {
            firebaseToken = firebaseService.decodeToken(firebaseAuthenticationToken.getCredentials().toString());
            assertThat(firebaseAuthenticationToken.getPrincipal()).isEqualTo(firebaseToken.getUid());
        } catch (FirebaseAuthException | AssertionError e) {
            throw new FirebaseAuthorizationException("Firebase Service doesn't accept your credentials", e);
        }
        SecurityUser securityUser = securityUserService.registerOrLogin(authentication);
        return convertToInternalToken(authentication, securityUser);
    }

    private Authentication convertToInternalToken(Authentication authentication, SecurityUser securityUser) {
        return new InternalSecurityToken(authentication.getAuthorities(), securityUser);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (FirebaseAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
