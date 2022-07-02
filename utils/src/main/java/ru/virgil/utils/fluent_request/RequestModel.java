package ru.virgil.utils.fluent_request;

import com.fasterxml.jackson.databind.JavaType;
import lombok.Data;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.annotation.Nullable;

@Data
class RequestModel {

    private RequestMethod requestMethod;
    private String url;
    @Nullable
    private Object requestBody;
    @Nullable
    private JavaType responseJavaType;
    private ResultMatcher[] resultMatchers;

}
