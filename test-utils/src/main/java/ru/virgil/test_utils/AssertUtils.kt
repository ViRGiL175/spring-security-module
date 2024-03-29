package ru.virgil.test_utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.truth.Truth
import org.springframework.stereotype.Component

/**
 * Помогает делать всякие сложные ассерты.
 */
@Component
class AssertUtils(private val objectMapper: ObjectMapper) {

    fun partialEquals(full: Any, partial: Any) {
        val fullJsonNode = objectMapper.valueToTree<JsonNode>(full)
        val fullFields = fullJsonNode.toList()
        val partialJsonNode = objectMapper.valueToTree<JsonNode>(partial)
        val partialFields = partialJsonNode.filterNotNull()
            .filter { !it.isNull }
            .toList()
        Truth.assertThat(fullFields).containsAtLeastElementsIn(partialFields)
    }
}
