package ru.virgil.spring_tools.examples.user

import org.springframework.security.test.context.support.TestExecutionEvent
import org.springframework.security.test.context.support.WithSecurityContext
import ru.virgil.spring_tools.examples.integration.roles.user.MockUserSecurityContextFactory
import ru.virgil.spring_tools.examples.security.UserAuthority

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = MockUserSecurityContextFactory::class, setupBefore = TestExecutionEvent.TEST_METHOD)
annotation class WithMockFirebaseUser(
    val authorities: Array<UserAuthority> = [UserAuthority.ROLE_USER],
    val firebaseUserId: String = "user-id",
    val firebaseAuthToken: String = "user-auth-token",
)
