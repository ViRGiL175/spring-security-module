package ru.virgil.utils.fluent_request;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;

import javax.annotation.Nullable;
import java.util.List;

@RequiredArgsConstructor
public class BodyStepMultipart implements BodyStepMultipartStart, BodyStep {

    private final RequestModel requestModel;
    private final Requester requester;
    private final ObjectMapper objectMapper;
    @Nullable
    private JavaType byteArrayJavaType;

    @Override
    public BodyStepMultipart file(MockMultipartFile mockMultipartFile) throws Exception {
        requestModel.setMockMultipartFile(mockMultipartFile);
        return this;
    }

    public BodyStepMultipart receive(Class<?> responseClass, Class<?>... responseClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(responseClass, responseClasses);
        requestModel.setResponseJavaType(javaType);
        return this;
    }

    public BodyStepMultipart receiveBytes() {
        byteArrayJavaType = objectMapper.getTypeFactory().constructParametricType(List.class, Byte.class);
        requestModel.setResponseJavaType(byteArrayJavaType);
        return this;
    }

    public FinalStep<?> and() throws Exception {
        if (requestModel.getResponseJavaType() == null) {
            return new FinalStepNoType(requestModel, requester);
        } else {
            if (requestModel.getResponseJavaType().equals(byteArrayJavaType)) {
                return new FinalStepByteType(requestModel, requester);
            } else {
                return new FinalStepJsonType<>(requestModel, requester, objectMapper);
            }
        }
    }
}
