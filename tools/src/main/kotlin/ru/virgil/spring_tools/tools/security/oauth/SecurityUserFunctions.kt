package ru.virgil.spring_tools.tools.security.oauth

import org.springframework.security.core.context.SecurityContextHolder
import ru.virgil.spring_tools.tools.security.internal.InternalAuthenticationToken

object SecurityUserFunctions {

    fun getPrincipal() = when (val auth = SecurityContextHolder.getContext().authentication) {
        is InternalAuthenticationToken -> auth.securityUser
        else -> throw SecurityException("Неизвестный токен в контексте безопасности")
    }

    fun getSecurityToken() = SecurityContextHolder.getContext().authentication!!
}
