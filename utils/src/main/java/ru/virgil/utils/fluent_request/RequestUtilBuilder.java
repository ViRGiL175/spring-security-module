package ru.virgil.utils.fluent_request;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

@RequiredArgsConstructor
public class RequestUtilBuilder {

    private final RequestModel requestModel;
    private final Requester requester;
    private final ObjectMapper objectMapper;

    public EmptyBodyBuilder send(Object dto) {
        requestModel.setRequestBody(dto);
        return new EmptyBodyBuilder();
    }

    public HavingBodyBuilder receive(Class<?> responseClass, Class<?>... responseClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(responseClass, responseClasses);
        requestModel.setResponseJavaType(javaType);
        return new HavingBodyBuilder();
    }

    public HavingBodyBuilder exchange(Object dto, Class<?> responseClass, Class<?>... responseClasses) {
        requestModel.setRequestBody(dto);
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(responseClass, responseClasses);
        requestModel.setResponseJavaType(javaType);
        return new HavingBodyBuilder();
    }

    public void expect(ResultMatcher... expectMatchers) throws Exception {
        new EmptyBodyBuilder().expect(expectMatchers);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class EmptyBodyBuilder {

        public void expect(ResultMatcher... expectMatchers) throws Exception {
            requestModel.setResultMatchers(expectMatchers);
            requester.makeRequest(requestModel);
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class HavingBodyBuilder {

        /**
         * При неуспешном статусе HTTP вернет null.
         * Но не помечаю @{@link javax.annotation.Nullable}, т.к. это ожидаемое поведение.
         * Иначе вечно будет напоминать о проверках на null.
         */
        @SuppressWarnings("ConstantConditions")
        public <ResponseDto> ResponseDto expect(ResultMatcher... expectMatchers) throws Exception {
            requestModel.setResultMatchers(expectMatchers);
            MvcResult mvcResult = requester.makeRequest(requestModel);
            if (HttpStatus.valueOf(mvcResult.getResponse().getStatus()).isError()) {
                return null;
            }
            String jsonBody = mvcResult.getResponse().getContentAsString();
            return objectMapper.readValue(jsonBody, requestModel.getResponseJavaType());
        }
    }
}
