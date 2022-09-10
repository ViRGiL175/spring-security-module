package ru.virgil.security;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    /**
     * По этим путям можно будет заходить анонимно
     */
    String[] anonymousPaths = new String[0];
    /**
     * Использовать для сессий заголовок X-Auth-Token вместо Cookies
     */
    boolean useXAuthToken = false;
    /**
     * Отключить редиректы после успешной авторизации
     */
    boolean disablePostAuthRedirect = false;

}
