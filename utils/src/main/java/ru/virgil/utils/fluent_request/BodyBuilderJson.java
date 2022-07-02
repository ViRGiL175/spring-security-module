package ru.virgil.utils.fluent_request;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.test.web.servlet.ResultMatcher;

@RequiredArgsConstructor
public class BodyBuilderJson {

    private final RequestModel requestModel;
    private final Requester requester;
    private final ObjectMapper objectMapper;

    public FinalBuilderEmptyBody send(Object dto) {
        requestModel.setRequestBody(dto);
        return new FinalBuilderEmptyBody(requestModel, requester);
    }

    public FinalBuilderHavingBody receive(Class<?> responseClass, Class<?>... responseClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(responseClass, responseClasses);
        requestModel.setResponseJavaType(javaType);
        return new FinalBuilderHavingBody(requestModel, requester, objectMapper);
    }

    public FinalBuilderHavingBody exchange(Object dto, Class<?> responseClass, Class<?>... responseClasses) {
        requestModel.setRequestBody(dto);
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(responseClass, responseClasses);
        requestModel.setResponseJavaType(javaType);
        return new FinalBuilderHavingBody(requestModel, requester, objectMapper);
    }

    public void expect(ResultMatcher... expectMatchers) throws Exception {
        new FinalBuilderEmptyBody(requestModel, requester).expect(expectMatchers);
    }
}
