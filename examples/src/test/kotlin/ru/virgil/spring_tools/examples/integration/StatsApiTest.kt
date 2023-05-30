package ru.virgil.spring_tools.examples.integration

import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import ru.virgil.spring_tools.examples.stats.StatsDto
import ru.virgil.spring_tools.examples.integration.roles.user.WithMockFirebaseUser
import ru.virgil.spring_tools.tools.testing.fluent.Fluent

@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
@WithMockFirebaseUser
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StatsApiTest @Autowired constructor(val fluent: Fluent) {

    @Test
    fun getAll() {
        val statsDto: StatsDto = fluent.request { get { "/stats/all" } }
        statsDto.shouldNotBeNull()
    }

    @Test
    fun getMy() {
        val statsDto: StatsDto = fluent.request { get { "/stats/my" } }
        statsDto.shouldNotBeNull()
    }

}
