package ru.virgil.spring_tools.tools.util

import net.datafaker.Faker
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class FakerProvider {

    @Bean
    fun provideFaker() = Faker()
}
