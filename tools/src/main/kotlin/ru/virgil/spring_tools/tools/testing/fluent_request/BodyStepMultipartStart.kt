package ru.virgil.spring_tools.tools.testing.fluent_request

import org.springframework.mock.web.MockMultipartFile

interface BodyStepMultipartStart {

    @Throws(Exception::class)
    fun file(mockMultipartFile: MockMultipartFile): BodyStepMultipart

}
