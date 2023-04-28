package ru.virgil.spring_tools.tools.testing.fluent_request

interface BodyStep {

    @Throws(Exception::class)
    fun and(): FinalStep<*>

}
