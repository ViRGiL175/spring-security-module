package ru.virgil.security.cors;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMethod;

@ConfigurationProperties(prefix = "security.custom-cors")
@Data
public class CorsProperties {

    private boolean useCustomCors = false;
    private String allowOrigin;
    private RequestMethod[] allowMethods;
    private int maxAge = 3600;
    private String[] allowHeaders;
    private String[] exposedHeaders;
    private boolean allowCredential;

}
