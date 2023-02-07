package ru.virgil.security.firebase

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security.firebase")
data class FirebaseSecurityProperties(
    val adminCredentialsFilePath: String,
    val webCredentialsFilePath: String,
    val authPageTemplateName: String,
)
