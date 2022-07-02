package ru.virgil.utils.fluent_request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RequestUtil {

    private final Requester requester;
    private final ObjectMapper objectMapper;

    private BodyBuilderJson startJsonBuilding(RequestMethod requestMethod, String path) {
        RequestModel requestModel = new RequestModel();
        requestModel.setRequestMethod(requestMethod);
        requestModel.setUrl(path);
        return new BodyBuilderJson(requestModel, requester, objectMapper);
    }

    private BodyBuilderMultipart startMultipartBuilding(RequestMethod requestMethod, String path) {
        RequestModel requestModel = new RequestModel();
        requestModel.setRequestMethod(requestMethod);
        requestModel.setUrl(path);
        return new BodyBuilderMultipart(requestModel, requester, objectMapper);
    }

    public BodyBuilderJson get(String path) {
        return startJsonBuilding(RequestMethod.GET, path);
    }

    public BodyBuilderJson post(String path) {
        return startJsonBuilding(RequestMethod.POST, path);
    }

    public BodyBuilderJson put(String path) {
        return startJsonBuilding(RequestMethod.PUT, path);
    }

    public BodyBuilderJson delete(String path) {
        return startJsonBuilding(RequestMethod.DELETE, path);
    }

    public BodyBuilderMultipart multipart(String path) {
        return startMultipartBuilding(RequestMethod.MULTIPART, path);
    }
}
