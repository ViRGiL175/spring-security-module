package ru.virgil.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@CommonsLog
public class TestUtils {

    public static final int JSON_OUTPUT_LENGTH = 2048;
    public static final String RESPONSE_TEXT_TEMPLATE = """
                     
                        
            /// TEST RESULTS ///
                        
            REQUEST: %s %s%s -> %d
                        
            REQUEST JSON:
            %s
                        
            RESPONSE JSON:
            %s  
            """;
    private final ObjectMapper objectMapper;

    // todo: изввлечение коллекций?
    public <T> T extractDtoFromResponse(MvcResult mvcResult, Class<T> dtoClass) throws IOException {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), dtoClass);
    }

    public void printResponse(MvcResult mvcResult) {
        try {
            String method = mvcResult.getRequest().getMethod();
            String requestURI = mvcResult.getRequest().getRequestURI();
            String requestParams = extractPrettyParams(mvcResult);
            int status = mvcResult.getResponse().getStatus();
            String responseContent = extractPrettyResponse(mvcResult, objectMapper);
            String requestContent = Optional.ofNullable(mvcResult.getRequest().getContentAsString())
                    .map(s -> extractPrettyRequest(objectMapper, s)).orElse("NONE");
            log.info(RESPONSE_TEXT_TEMPLATE.formatted(method, requestURI, requestParams, status, requestContent,
                    responseContent));
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractPrettyRequest(ObjectMapper objectMapper, String requestContent) {
        try {
            requestContent = objectMapper.readTree(requestContent).toPrettyString();
            requestContent = shortenJson(requestContent);
            return requestContent;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String shortenJson(String requestContent) {
        String substring = requestContent.substring(0, Math.min(JSON_OUTPUT_LENGTH, requestContent.length()));
        if (substring.length() == JSON_OUTPUT_LENGTH) {
            substring = substring + """
                    ...
                                        
                    /// JSON SHORTENED TO %d SYMBOLS ///
                                        
                    """.formatted(JSON_OUTPUT_LENGTH);
        }
        return substring;
    }

    private String extractPrettyResponse(MvcResult mvcResult, ObjectMapper objectMapper)
            throws UnsupportedEncodingException, JsonProcessingException {
        String responseContent = mvcResult.getResponse().getContentAsString();
        responseContent = objectMapper.readTree(responseContent).toPrettyString();
        responseContent = shortenJson(responseContent);
        if (responseContent.isEmpty()) {
            return "NONE";
        }
        return responseContent;
    }

    private String extractPrettyParams(MvcResult mvcResult) {
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
