package ru.virgil.example.util.security.user

import org.springframework.security.test.context.support.TestExecutionEvent
import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = MockUserSecurityContextFactory::class, setupBefore = TestExecutionEvent.TEST_METHOD)
annotation class WithMockFirebaseUser(
    val firebaseUserId: String = "",
    val firebaseAuthToken: String = "",
)
