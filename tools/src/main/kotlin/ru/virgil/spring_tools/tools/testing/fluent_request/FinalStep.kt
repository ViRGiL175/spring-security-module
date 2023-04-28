package ru.virgil.spring_tools.tools.testing.fluent_request

import org.springframework.test.web.servlet.ResultMatcher

interface FinalStep<T> {

    @Throws(Exception::class)
    fun expect(vararg expectMatchers: ResultMatcher): T
}
