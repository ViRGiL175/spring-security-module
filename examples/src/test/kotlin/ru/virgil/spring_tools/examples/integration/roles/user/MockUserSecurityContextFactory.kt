package ru.virgil.spring_tools.examples.integration.roles.user

import org.springframework.security.core.context.SecurityContext
import org.springframework.security.test.context.support.WithSecurityContextFactory
import org.springframework.stereotype.Component
import ru.virgil.spring_tools.examples.security.SecurityUserDetailsService
import ru.virgil.spring_tools.examples.user.WithMockFirebaseUser
import ru.virgil.spring_tools.tools.security.mock.MockSecurityContextFactory


@Component
class MockUserSecurityContextFactory(
    securityUserDetailsService: SecurityUserDetailsService,
) : WithSecurityContextFactory<WithMockFirebaseUser>, MockSecurityContextFactory(securityUserDetailsService) {

    override fun createSecurityContext(annotation: WithMockFirebaseUser): SecurityContext =
        createSecurityContext(annotation.firebaseUserId, annotation.firebaseAuthToken, *annotation.authorities)

}
