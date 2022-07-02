package ru.virgil.utils.fluent_request;

import lombok.RequiredArgsConstructor;
import org.springframework.test.web.servlet.ResultMatcher;

@RequiredArgsConstructor
public class FinalBuilderEmptyBody {

    private final RequestModel requestModel;
    private final Requester requester;

    public void expect(ResultMatcher... expectMatchers) throws Exception {
        requestModel.setResultMatchers(expectMatchers);
        requester.makeRequest(requestModel);
    }
}
