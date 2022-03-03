package ru.virgil.example.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.virgil.example.user.UserDetails;
import ru.virgil.example.user.UserDetailsService;
import ru.virgil.security.entity.SecurityUser;
import ru.virgil.security.firebase.FirebaseAuthenticationToken;

import java.util.Set;

@Primary
@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements ru.virgil.security.service.SecurityUserService {

    @Getter
    private final SecurityUserDetailsRepository repository;
    private final UserDetailsService userDetailsService;
    private final OnUserRegistrationMockService onUserRegistrationMockService;

    @Override
    public SecurityUser registerOrLogin(Authentication authentication) {
        if (!(authentication instanceof FirebaseAuthenticationToken firebaseAuthenticationToken)) {
            throw new AuthenticationServiceException("Wrong %s token type in %s".formatted(
                    authentication.getClass().getSimpleName(), this.getClass().getSimpleName()));
        }
        return repository.findByFirebaseUserId(firebaseAuthenticationToken.getPrincipal().toString())
                .orElseGet(() -> register(firebaseAuthenticationToken));
    }

    public SecurityUserDetails register(FirebaseAuthenticationToken firebaseAuthenticationToken) {
        SecurityUserDetails securityUserDetails = new SecurityUserDetails();
        securityUserDetails.setFirebaseUserId(firebaseAuthenticationToken.getPrincipal().toString());
        securityUserDetails.setAuthorities(Set.of(UserAuthority.USER));
        securityUserDetails = repository.save(securityUserDetails);
        UserDetails userDetails = new UserDetails();
        userDetails.setSecurityUserDetails(securityUserDetails);
        userDetails = userDetailsService.getRepository().save(userDetails);
        userDetails = onUserRegistrationMockService.mock(userDetails);
        userDetails = userDetailsService.getRepository().save(userDetails);
        return securityUserDetails;
    }

}
