package ru.virgil.test_utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.truth.Truth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Помогает делать всякие сложные ассерты.
 */
@Component
@RequiredArgsConstructor
public class AssertUtils {

    private final ObjectMapper objectMapper;

    public void partialEquals(Object full, Object partial) {
        List<JsonNode> fullFields = StreamSupport.stream(objectMapper.valueToTree(full).spliterator(), false)
                .toList();
        List<JsonNode> partialFields = StreamSupport.stream(objectMapper.valueToTree(partial).spliterator(), false)
                .filter(jsonNode -> !jsonNode.isNull())
                .toList();
        Truth.assertThat(fullFields).containsAtLeastElementsIn(partialFields);
    }

}
