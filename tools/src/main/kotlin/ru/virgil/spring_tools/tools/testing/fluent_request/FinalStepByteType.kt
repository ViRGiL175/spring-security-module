package ru.virgil.spring_tools.tools.testing.fluent_request

import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultMatcher

class FinalStepByteType(
    private val requestModel: RequestModel,
    private val requester: Requester,
) : FinalStep<ByteArray> {

    @Throws(Exception::class)
    override fun expect(vararg expectMatchers: ResultMatcher): ByteArray {
        requestModel.resultMatchers = (expectMatchers.toList())
        val mvcResult: MvcResult = requester.makeRequest(requestModel)
        return mvcResult.response.contentAsByteArray
    }
}
