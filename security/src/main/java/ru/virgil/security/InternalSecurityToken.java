package ru.virgil.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import ru.virgil.security.entity.SecurityUser;

import javax.annotation.Nullable;
import java.util.Collection;

public class InternalSecurityToken extends AbstractAuthenticationToken {

    private final SecurityUser securityUser;

    /**
     * Авторизованный внутренний токен, который уходит вглубь системы, не должен знать авторизационных данных.
     * <p>
     * Хранит в себе ID и сам объект пользователя
     */
    public InternalSecurityToken(Collection<? extends GrantedAuthority> authorities, SecurityUser securityUser) {
        super(authorities);
        this.securityUser = securityUser;
        setAuthenticated(true);
    }

    /**
     * @return null, т.к. внутри сервера мы не должны знать никакой авторизационной информации пользователя
     */
    @Override
    @Nullable
    public Void getCredentials() {
        return null;
    }

    @Override
    public SecurityUser getPrincipal() {
        return securityUser;
    }
}
