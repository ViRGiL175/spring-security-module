package ru.virgil.spring_tools.examples.integration.roles.user

import org.springframework.security.test.context.support.TestExecutionEvent
import org.springframework.security.test.context.support.WithSecurityContext
import ru.virgil.spring_tools.examples.security.SecurityUserAuthority

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = MockUserSecurityContextFactory::class, setupBefore = TestExecutionEvent.TEST_METHOD)
annotation class WithMockFirebaseUser(
    val authorities: Array<SecurityUserAuthority> = [SecurityUserAuthority.ROLE_USER],
    val firebaseUserId: String = "user-id",
    val firebaseAuthToken: String = "user-auth-token",
)
