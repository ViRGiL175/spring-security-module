package ru.virgil.spring_tools

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackages = ["ru.virgil.spring_tools"])
@EnableJpaAuditing
class ExamplesApplication

fun main(args: Array<String>) {
    runApplication<ExamplesApplication>(*args)
}
