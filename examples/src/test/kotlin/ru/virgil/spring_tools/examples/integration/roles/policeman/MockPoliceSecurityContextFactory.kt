package ru.virgil.spring_tools.examples.integration.roles.policeman

import org.springframework.security.core.context.SecurityContext
import org.springframework.security.test.context.support.WithSecurityContextFactory
import org.springframework.stereotype.Component
import ru.virgil.spring_tools.examples.security.SecurityUserDetailsService
import ru.virgil.spring_tools.tools.security.mock.MockSecurityContextFactory

@Component
class MockPoliceSecurityContextFactory(
    securityUserDetailsService: SecurityUserDetailsService,
) : WithSecurityContextFactory<WithMockFirebasePoliceman>, MockSecurityContextFactory(securityUserDetailsService) {

    override fun createSecurityContext(annotation: WithMockFirebasePoliceman): SecurityContext =
        createSecurityContext(annotation.firebaseUserId, annotation.firebaseAuthToken, *annotation.authorities)

}
