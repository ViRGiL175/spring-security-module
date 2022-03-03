package ru.virgil.example.util;

import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(
        factory = TestSecurityContextFactory.class,
        setupBefore = TestExecutionEvent.TEST_METHOD
)
public @interface WithMockFirebaseUser {

    String firebaseUserId() default "firebase-user-id";

    String firebaseAuthToken() default "firebase-auth-token";

}
