package ru.virgil.spring_tools.tools.testing.fluent_request

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultMatcher

class FinalStepJsonType<ResponseDto>(
    private val requestModel: RequestModel,
    private val requester: Requester,
    private val objectMapper: ObjectMapper,
) : FinalStep<ResponseDto?> {

    /**
     * При неуспешном статусе HTTP вернет null.
     * Но не помечаю @[javax.annotation.Nullable], т.к. это ожидаемое поведение.
     * Иначе вечно будет напоминать о проверках на null.
     */
    @Throws(Exception::class)
    override fun expect(vararg expectMatchers: ResultMatcher): ResponseDto? {
        requestModel.resultMatchers = (expectMatchers.toList())
        val mvcResult: MvcResult = requester.makeRequest(requestModel)
        if (HttpStatus.valueOf(mvcResult.response.status).isError) {
            return null
        }
        val jsonBody: String = mvcResult.response.contentAsString
        return objectMapper.readValue(jsonBody, requestModel.responseJavaType)
    }
}
