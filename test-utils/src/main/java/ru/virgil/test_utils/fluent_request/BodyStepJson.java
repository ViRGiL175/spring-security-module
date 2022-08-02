package ru.virgil.test_utils.fluent_request;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;

@RequiredArgsConstructor
public class BodyStepJson implements BodyStepJsonStart, BodyStep {

    private final RequestModel requestModel;
    private final Requester requester;
    private final ObjectMapper objectMapper;
    @Nullable
    private JavaType byteArrayJavaType;

    @Override
    public BodyStepJson send(Object dto) {
        requestModel.setRequestBody(dto);
        return this;
    }

    @Override
    public BodyStepJson receive(Class<?> responseClass, Class<?>... responseClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(responseClass, responseClasses);
        requestModel.setResponseJavaType(javaType);
        return this;
    }

    @Override
    public BodyStepJson receiveAsBytes() {
        byteArrayJavaType = objectMapper.getTypeFactory().constructParametricType(List.class, Byte.class);
        requestModel.setResponseJavaType(byteArrayJavaType);
        return this;
    }

    @Override
    public BodyStepJson exchange(Object dto, Class<?> responseClass, Class<?>... responseClasses) {
        requestModel.setRequestBody(dto);
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(responseClass, responseClasses);
        requestModel.setResponseJavaType(javaType);
        return this;
    }

    @Override
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
