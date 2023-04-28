package ru.virgil.spring_tools.tools.testing.fluent_request

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.ObjectProvider
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import ru.virgil.spring_tools.tools.testing.TestUtils
import java.util.*

@Component
class Requester(
    private val mockMvcProvider: ObjectProvider<MockMvc>,
    private val testUtils: TestUtils,
    private val jackson: ObjectMapper,
) {

    private val mockMvc by lazy { mockMvcProvider.getObject() }

    private fun getHttpBuilder(requestMethod: RequestMethod, url: String): MockHttpServletRequestBuilder {
        return when (requestMethod) {
            RequestMethod.GET -> MockMvcRequestBuilders.get(url)
            RequestMethod.POST -> MockMvcRequestBuilders.post(url)
            RequestMethod.PUT -> MockMvcRequestBuilders.put(url)
            RequestMethod.DELETE -> MockMvcRequestBuilders.delete(url)
            RequestMethod.POST_MULTIPART -> MockMvcRequestBuilders.multipart(url)
        }
    }

    @Throws(JsonProcessingException::class)
    private fun addBody(builder: MockHttpServletRequestBuilder, objectBody: Any) {
        val jsonBody = jackson.writeValueAsString(objectBody)
        builder.content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
    }

    private fun addFile(builder: MockMultipartHttpServletRequestBuilder, mockMultipartFile: MockMultipartFile) {
        builder.file(mockMultipartFile)
    }

    @Throws(Exception::class)
    fun makeRequest(requestModel: RequestModel): MvcResult {
        val builder: MockHttpServletRequestBuilder =
            getHttpBuilder(requestModel.requestMethod!!, requestModel.url!!)
        if (Optional.ofNullable(requestModel.requestBody).isPresent) {
            addBody(builder, requestModel.requestBody!!)
        } else if (Optional.ofNullable(requestModel.mockMultipartFile).isPresent) {
            addFile(builder as MockMultipartHttpServletRequestBuilder, requestModel.mockMultipartFile!!)
        }
        return mockMvc.perform(builder)
            .andExpectAll(*requestModel.resultMatchers.toTypedArray())
            .andDo { mvcResult: MvcResult? -> testUtils.printResponse(mvcResult!!) }
            .andReturn()
    }

}
