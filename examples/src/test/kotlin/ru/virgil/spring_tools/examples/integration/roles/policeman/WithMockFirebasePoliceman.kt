package ru.virgil.spring_tools.examples.integration.roles.policeman

import org.springframework.security.test.context.support.TestExecutionEvent
import org.springframework.security.test.context.support.WithSecurityContext
import ru.virgil.spring_tools.examples.security.UserAuthority

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = MockPoliceSecurityContextFactory::class, setupBefore = TestExecutionEvent.TEST_METHOD)
annotation class WithMockFirebasePoliceman(
    val authorities: Array<UserAuthority> = [UserAuthority.ROLE_POLICE],
    val firebaseUserId: String = "police-id",
    val firebaseAuthToken: String = "police-auth-token",
)
