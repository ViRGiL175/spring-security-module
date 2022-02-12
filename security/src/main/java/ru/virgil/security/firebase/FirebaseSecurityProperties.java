package ru.virgil.security.firebase;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.firebase")
public record FirebaseSecurityProperties(
        String adminCredentialsFilePath,
        String webCredentialsFilePath,
        String authPageTemplateName
) {

}
