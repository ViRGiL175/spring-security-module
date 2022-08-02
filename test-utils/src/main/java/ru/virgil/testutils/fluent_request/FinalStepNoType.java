package ru.virgil.testutils.fluent_request;

import com.google.common.truth.Truth;
import lombok.RequiredArgsConstructor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

@RequiredArgsConstructor
public class FinalStepNoType implements FinalStep<MvcResult> {

    private final RequestModel requestModel;
    private final Requester requester;

    public MvcResult expect(ResultMatcher... expectMatchers) throws Exception {
        Truth.assertThat(requestModel.getResponseJavaType()).isNull();
        requestModel.setResultMatchers(expectMatchers);
        return requester.makeRequest(requestModel);
    }
}
