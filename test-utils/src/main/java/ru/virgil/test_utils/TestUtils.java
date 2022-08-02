package ru.virgil.test_utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@CommonsLog
public class TestUtils {

    // todo: оставить тут только работу с парсингом
    public static final int JSON_OUTPUT_LENGTH = 2048;
    public static final String TEST_RESULTS_TEMPLATE = """
                     
                        
            /// TEST RESULTS ///
                        
            REQUEST: %s %s%s -> %d
                        
            REQUEST JSON:
            %s
                        
            RESPONSE JSON:
            %s
            """;
    protected final ObjectMapper objectMapper;

    // todo: изввлечение коллекций?
    public <D> D extractDtoFromResponse(MvcResult mvcResult, Class<D> dtoClass) throws IOException {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), dtoClass);
    }

    @SuppressWarnings("rawtypes")
    public <D, C extends Collection, R> R extractDtoFromResponse(MvcResult mvcResult, Class<C> collectionClass,
            Class<D> dtoClass) throws IOException {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(collectionClass, dtoClass);
        String contentAsString = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(contentAsString, collectionType);
    }

    public void printResponse(MvcResult mvcResult) {
        try {
            String method = mvcResult.getRequest().getMethod();
            String requestURI = mvcResult.getRequest().getRequestURI();
            String requestParams = extractPrettyParams(mvcResult);
            int status = mvcResult.getResponse().getStatus();
            String responseContent = extractPrettyResponse(mvcResult);
            String requestContent = Optional.ofNullable(mvcResult.getRequest().getContentAsString())
                    .map(this::extractPrettyRequest).orElse("NONE");
            log.info(TEST_RESULTS_TEMPLATE.formatted(method, requestURI, requestParams, status, requestContent,
                    responseContent));
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected String extractPrettyRequest(String requestContent) {
        try {
            requestContent = objectMapper.readTree(requestContent).toPrettyString();
            requestContent = shortenJson(requestContent);
            return requestContent;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected String extractPrettyResponse(MvcResult mvcResult)
            throws UnsupportedEncodingException, JsonProcessingException {
        String responseContent = mvcResult.getResponse().getContentAsString();
        if (!isJson(responseContent)) {
            return "BODY: content-type -> " + mvcResult.getResponse().getContentType();
        }
        responseContent = objectMapper.readTree(responseContent).toPrettyString();
        responseContent = shortenJson(responseContent);
        if (responseContent.isEmpty()) {
            return "NONE";
        }
        return responseContent;
    }

    public boolean isJson(String jsonInString) {
        try {
            new JSONObject(jsonInString);
        } catch (JSONException ex) {
            try {
                new JSONArray(jsonInString);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    protected String shortenJson(String requestContent) {
        String substring = requestContent.substring(0, Math.min(JSON_OUTPUT_LENGTH, requestContent.length()));
        if (substring.length() == JSON_OUTPUT_LENGTH) {
            substring = substring + """
                    ...
                                        
                    /// JSON SHORTENED TO %d SYMBOLS ///
                                        
                    """.formatted(JSON_OUTPUT_LENGTH);
        }
        return substring;
    }

    protected String extractPrettyParams(MvcResult mvcResult) {
        Map<String, String[]> parameterMap = mvcResult.getRequest().getParameterMap();
        String stringParams = parameterMap.keySet().stream()
                .map(key -> key + "=" + String.join(",", parameterMap.get(key)))
                .collect(Collectors.joining("&", "?", ""));
        if (stringParams.equals("?")) {
            stringParams = "";
        }
        return stringParams;
    }

}
