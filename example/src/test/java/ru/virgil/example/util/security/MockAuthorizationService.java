package ru.virgil.example.util.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.virgil.example.security.SecurityUserDetails;
import ru.virgil.example.security.SecurityUserDetailsService;
import ru.virgil.security.firebase.FirebaseAuthenticationToken;

@Service
@RequiredArgsConstructor
public class MockAuthorizationService implements ru.virgil.security.mock.MockAuthorizationService {

    private final SecurityUserDetailsService securityUserDetailsService;

    @Override
    public SecurityUserDetails authOrRegister(FirebaseAuthenticationToken token) {
        return securityUserDetailsService.getRepository().findByFirebaseUserId(token.getPrincipal().toString())
                .orElseGet(() -> securityUserDetailsService.register(token));
    }
}
