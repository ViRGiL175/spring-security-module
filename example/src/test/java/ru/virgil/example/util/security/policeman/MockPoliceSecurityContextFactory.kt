package ru.virgil.example.util.security.policeman

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.stereotype.Component
import ru.virgil.example.security.UserAuthority
import ru.virgil.example.util.security.MockAuthorizationService
import ru.virgil.security.mock.MockSecurityContextFactory

@Component
class MockPoliceSecurityContextFactory(
    mockAuthorizationService: MockAuthorizationService,
) : MockSecurityContextFactory<WithMockFirebasePoliceman>(mockAuthorizationService) {

    override fun createSecurityContext(annotation: WithMockFirebasePoliceman): SecurityContext {
        val authorities = mutableSetOf<GrantedAuthority>(SimpleGrantedAuthority(UserAuthority.ROLE_POLICE.name))
        return authenticate(annotation.firebaseUserId, annotation.firebaseAuthToken, authorities)
    }
}
