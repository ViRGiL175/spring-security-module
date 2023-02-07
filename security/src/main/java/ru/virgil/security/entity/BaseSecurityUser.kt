package ru.virgil.security.entity

import org.springframework.security.core.GrantedAuthority


class BaseSecurityUser(
    override val authorities: Collection<GrantedAuthority>,
    override val isBanned: Boolean = false,
    override val firebaseUserId: String,
) : SecurityUser
