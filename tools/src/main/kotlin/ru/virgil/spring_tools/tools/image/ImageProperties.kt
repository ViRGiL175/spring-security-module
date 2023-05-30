package ru.virgil.spring_tools.tools.image

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "images")
data class ImageProperties(val cleanOnDestroy: Boolean = true)
