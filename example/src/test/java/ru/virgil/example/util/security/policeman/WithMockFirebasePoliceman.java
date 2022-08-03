package ru.virgil.example.util.security.policeman;

import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(
        factory = MockPoliceSecurityContextFactory.class,
        setupBefore = TestExecutionEvent.TEST_METHOD
)
public @interface WithMockFirebasePoliceman {

    String firebaseUserId() default "";

    String firebaseAuthToken() default "";

}
