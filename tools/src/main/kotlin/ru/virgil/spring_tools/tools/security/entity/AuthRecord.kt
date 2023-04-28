package ru.virgil.spring_tools.tools.security.entity


data class AuthRecord(
    val principal: String?,
    val credentials: String?,
)
