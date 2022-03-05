package ru.virgil.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.Nullable;

@ConfigurationProperties(prefix = "security")
public record SecurityProperties(@Nullable String... anonymousPaths) {

}
