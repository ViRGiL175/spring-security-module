package ru.virgil.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@ConfigurationPropertiesScan
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true)
@SpringBootApplication(scanBasePackages = {"ru.virgil.example", "ru.virgil.security", "ru.virgil.utils"})
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

}
