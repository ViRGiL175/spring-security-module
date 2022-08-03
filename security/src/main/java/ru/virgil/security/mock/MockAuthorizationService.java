package ru.virgil.security.mock;

import ru.virgil.security.entity.SecurityUser;
import ru.virgil.security.firebase.FirebaseAuthenticationToken;

public interface MockAuthorizationService {

    SecurityUser authOrRegister(FirebaseAuthenticationToken token);

}
