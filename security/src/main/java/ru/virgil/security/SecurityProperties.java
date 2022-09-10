package ru.virgil.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    /**
     * По этим путям можно будет заходить анонимно
     */
    private String[] anonymousPaths = new String[0];
    /**
     * Использовать для сессий заголовок X-Auth-Token вместо Cookies
     */
    private boolean useXAuthToken = false;
    /**
     * Отключить редиректы после успешной авторизации
     */
    private boolean disablePostAuthRedirect = false;
}
