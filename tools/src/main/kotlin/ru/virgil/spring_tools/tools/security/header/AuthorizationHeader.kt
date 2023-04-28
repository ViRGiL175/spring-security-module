package ru.virgil.spring_tools.tools.security.header

import ru.virgil.spring_tools.tools.security.entity.AuthMethods

data class AuthorizationHeader(
    val authScheme: AuthMethods,
    val credentials: Map<String, String>,
)
