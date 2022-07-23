package ru.virgil.example.util.security.user;

import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(
        factory = TestSecurityContextFactoryUser.class,
        setupBefore = TestExecutionEvent.TEST_METHOD
)
public @interface WithMockFirebaseUser {

    String firebaseUserId() default "firebase-user-id";

    String firebaseAuthToken() default "firebase-auth-token";

}
