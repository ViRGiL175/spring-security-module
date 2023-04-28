package ru.virgil.spring_tools.tools.security.firebase

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security.firebase")
data class FirebaseSecurityProperties(
    var adminCredentialsFilePath: String? = null,
    var webCredentialsFilePath: String? = null,
    var authPageTemplateName: String? = null,
)
