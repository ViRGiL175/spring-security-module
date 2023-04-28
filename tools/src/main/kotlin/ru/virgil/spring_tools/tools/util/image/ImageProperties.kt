package ru.virgil.spring_tools.tools.util.image

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "images")
data class ImageProperties(val cleanOnDestroy: Boolean = true)
