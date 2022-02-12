package ru.virgil.security.firebase;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import ru.virgil.security.InternalSecurityToken;

import java.util.Collection;

public class FirebaseAuthenticationToken extends AbstractAuthenticationToken {

    private final String firebaseUid;
    private final String firebaseAuthToken;

    /**
     * Внешний токен, который содержит данные для авторизации Firebase.
     * В провайдере должен быть превращен в {@link InternalSecurityToken}
     *
     * @param authorities       the collection of {@link GrantedAuthority}s for the principal
     *                          represented by this authentication object.
     * @param firebaseUid       Идентификатор пользователя Firebase
     * @param firebaseAuthToken Токен для авторизации Firebase
     */
    public FirebaseAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String firebaseUid,
            String firebaseAuthToken) {
        super(authorities);
        this.firebaseUid = firebaseUid;
        this.firebaseAuthToken = firebaseAuthToken;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return firebaseAuthToken;
    }

    @Override
    public Object getPrincipal() {
        return firebaseUid;
    }
}
