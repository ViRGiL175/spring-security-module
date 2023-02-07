package ru.virgil.security

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "security")
data class SecurityProperties(
    /**
     * По этим путям можно будет заходить анонимно
     */
    val anonymousPaths: List<String> = listOf(),
    /**
     * Использовать для сессий заголовок X-Auth-Token вместо Cookies
     */
    val useXAuthToken: Boolean = false,
    /**
     * Отключить редиректы после успешной авторизации
     */
    val disablePostAuthRedirect: Boolean = false,
)
