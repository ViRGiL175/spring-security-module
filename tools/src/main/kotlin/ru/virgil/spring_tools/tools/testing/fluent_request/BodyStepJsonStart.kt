package ru.virgil.spring_tools.tools.testing.fluent_request

interface BodyStepJsonStart {

    fun send(dto: Any): BodyStepJson
    fun receive(responseClass: Class<*>, vararg responseClasses: Class<*>): BodyStepJson
    fun receiveAsBytes(): BodyStepJson
    fun exchange(dto: Any, responseClass: Class<*>, vararg responseClasses: Class<*>): BodyStepJson

    @Throws(Exception::class)
    fun and(): FinalStep<*>
}
