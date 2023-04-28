package ru.virgil.spring_tools.tools.testing.fluent_request

import com.google.common.truth.Truth
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultMatcher

class FinalStepNoType(
    private val requestModel: RequestModel,
    private val requester: Requester,
) : FinalStep<MvcResult> {

    @Throws(Exception::class)
    override fun expect(vararg expectMatchers: ResultMatcher): MvcResult {
        Truth.assertThat(requestModel.responseJavaType).isNull()
        requestModel.resultMatchers = (expectMatchers.toList())
        return requester.makeRequest(requestModel)
    }

}
