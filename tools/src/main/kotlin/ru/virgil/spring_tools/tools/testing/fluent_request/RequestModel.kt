package ru.virgil.spring_tools.tools.testing.fluent_request

import com.fasterxml.jackson.databind.JavaType

import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.ResultMatcher


data class RequestModel(
    var requestMethod: RequestMethod? = null,
    var url: String? = null,
    var requestBody: Any? = null,
    var responseJavaType: JavaType? = null,
    var mockMultipartFile: MockMultipartFile? = null,
    var resultMatchers: List<ResultMatcher> = mutableListOf(),
)
