package ru.virgil.utils.fluent_request;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;

@RequiredArgsConstructor
public class BodyBuilderMultipart {

    private final RequestModel requestModel;
    private final Requester requester;
    private final ObjectMapper objectMapper;

    public FinalBuilderMultipartBody file(MockMultipartFile mockMultipartFile) throws Exception {
        requestModel.setMockMultipartFile(mockMultipartFile);
        return new FinalBuilderMultipartBody(requestModel, requester);
    }

    public FinalBuilderHavingBody receive(Class<?> responseClass, Class<?>... responseClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(responseClass, responseClasses);
        requestModel.setResponseJavaType(javaType);
        return new FinalBuilderHavingBody(requestModel, requester, objectMapper);
    }
}
