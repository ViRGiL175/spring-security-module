package ru.virgil.test_utils.fluent_request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

@RequiredArgsConstructor
public class FinalStepJsonType<ResponseDto> implements FinalStep<ResponseDto> {

    private final RequestModel requestModel;
    private final Requester requester;
    private final ObjectMapper objectMapper;

    /**
     * При неуспешном статусе HTTP вернет null.
     * Но не помечаю @{@link javax.annotation.Nullable}, т.к. это ожидаемое поведение.
     * Иначе вечно будет напоминать о проверках на null.
     */
    @SuppressWarnings("ConstantConditions")
    public ResponseDto expect(ResultMatcher... expectMatchers) throws Exception {
        requestModel.setResultMatchers(expectMatchers);
        MvcResult mvcResult = requester.makeRequest(requestModel);
        if (HttpStatus.valueOf(mvcResult.getResponse().getStatus()).isError()) {
            return null;
        }
        String jsonBody = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(jsonBody, requestModel.getResponseJavaType());
    }
}
