package ru.virgil.security.firebase;

import org.springframework.security.core.AuthenticationException;

public class FirebaseAuthorizationException extends AuthenticationException {

    public FirebaseAuthorizationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public FirebaseAuthorizationException(String msg) {
        super(msg);
    }
}
