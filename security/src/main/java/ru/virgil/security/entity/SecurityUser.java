package ru.virgil.security.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface SecurityUser {

    Collection<? extends GrantedAuthority> getAuthorities();

    boolean isBanned();

    String getFirebaseUserId();
}
