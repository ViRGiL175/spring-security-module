package ru.virgil.utils.fluent_request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RequestUtil {

    private final Requester requester;
    private final ObjectMapper objectMapper;

    private RequestUtilBuilder createRequestUtilBuilder(RequestMethod requestType, String path) {
        RequestModel requestModel = new RequestModel();
        requestModel.setRequestMethod(requestType);
        requestModel.setUrl(path);
        return new RequestUtilBuilder(requestModel, requester, objectMapper);
    }

    public RequestUtilBuilder get(String path) {
        return createRequestUtilBuilder(RequestMethod.GET, path);
    }

    public RequestUtilBuilder post(String path) {
        return createRequestUtilBuilder(RequestMethod.POST, path);
    }

    public RequestUtilBuilder put(String path) {
        return createRequestUtilBuilder(RequestMethod.PUT, path);
    }

    public RequestUtilBuilder delete(String path) {
        return createRequestUtilBuilder(RequestMethod.DELETE, path);
    }
}
