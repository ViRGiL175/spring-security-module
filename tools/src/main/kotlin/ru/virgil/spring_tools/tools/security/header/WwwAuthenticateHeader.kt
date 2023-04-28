package ru.virgil.spring_tools.tools.security.header

import ru.virgil.spring_tools.tools.security.entity.AuthMethods


data class WwwAuthenticateHeader(
    val authScheme: AuthMethods,
    val challenges: Map<String, String>,
)
