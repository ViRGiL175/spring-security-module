package ru.virgil.security.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class BaseSecurityUser implements SecurityUser {

    private final Collection<? extends GrantedAuthority> authorities;
    private boolean banned = false;
    private String firebaseUserId;

    public BaseSecurityUser(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
