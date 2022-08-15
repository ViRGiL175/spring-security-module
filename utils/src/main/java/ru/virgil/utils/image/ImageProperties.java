package ru.virgil.utils.image;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "images")
public record ImageProperties(
        boolean cleanOnDestroy
) {

}
