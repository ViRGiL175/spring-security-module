package ru.virgil.example.util.security.policeman

import org.springframework.security.test.context.support.TestExecutionEvent
import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = MockPoliceSecurityContextFactory::class, setupBefore = TestExecutionEvent.TEST_METHOD)
annotation class WithMockFirebasePoliceman(
    val firebaseUserId: String = "",
    val firebaseAuthToken: String = "",
)
