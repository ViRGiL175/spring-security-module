package ru.virgil.utils.fluent_request;

import lombok.RequiredArgsConstructor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

@RequiredArgsConstructor
public class FinalStepByteType implements FinalStep<byte[]> {

    private final RequestModel requestModel;
    private final Requester requester;

    public byte[] expect(ResultMatcher... expectMatchers) throws Exception {
        requestModel.setResultMatchers(expectMatchers);
        MvcResult mvcResult = requester.makeRequest(requestModel);
        return mvcResult.getResponse().getContentAsByteArray();
    }
}
