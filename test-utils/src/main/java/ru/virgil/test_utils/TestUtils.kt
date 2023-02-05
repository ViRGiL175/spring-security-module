package ru.virgil.test_utils

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MvcResult
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.util.*
import java.util.stream.Collectors
import kotlin.math.min

@Component
class TestUtils(protected val objectMapper: ObjectMapper) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    // todo: изввлечение коллекций?
    fun <D> extractDtoFromResponse(mvcResult: MvcResult, dtoClass: Class<D>): D {
        return objectMapper.readValue(mvcResult.response.contentAsString, dtoClass)
    }

    @Throws(IOException::class)
    fun <D, C : Collection<*>, R> extractDtoFromResponse(
        mvcResult: MvcResult, collectionClass: Class<C>,
        dtoClass: Class<D>,
    ): R {
        val typeFactory = objectMapper.typeFactory
        val collectionType = typeFactory.constructCollectionType(collectionClass, dtoClass)
        val contentAsString = mvcResult.response.contentAsString
        return objectMapper.readValue(contentAsString, collectionType)
    }

    fun printResponse(mvcResult: MvcResult) {
        val method = mvcResult.request.method
        val requestURI = mvcResult.request.requestURI
        val requestParams = extractPrettyParams(mvcResult)
        val status = mvcResult.response.status
        val responseContent = extractPrettyResponse(mvcResult)
        val requestContent = Optional.ofNullable(mvcResult.request.contentAsString)
            .map { extractPrettyRequest(it) }
            .orElse("NONE")
        logger.info(
            TEST_RESULTS_TEMPLATE.format(
                method,
                requestURI,
                requestParams,
                status,
                requestContent,
                responseContent
            )
        )
    }

    protected fun extractPrettyRequest(requestContent: String): String {
        var requestContent = requestContent
        requestContent = objectMapper.readTree(requestContent).toPrettyString()
        requestContent = shortenJson(requestContent)
        return requestContent
    }

    @Throws(UnsupportedEncodingException::class, JsonProcessingException::class)
    protected fun extractPrettyResponse(mvcResult: MvcResult): String {
        var responseContent = mvcResult.response.contentAsString
        if (!isJson(responseContent)) {
            return "BODY: content-type -> ${mvcResult.response.contentType}"
        }
        responseContent = objectMapper.readTree(responseContent).toPrettyString()
        responseContent = shortenJson(responseContent)
        return if (responseContent.isEmpty()) {
            "NONE"
        } else responseContent
    }

    fun isJson(jsonInString: String): Boolean {
        try {
            JSONObject(jsonInString)
        } catch (ex: JSONException) {
            try {
                JSONArray(jsonInString)
            } catch (ex1: JSONException) {
                return false
            }
        }
        return true
    }

    protected fun shortenJson(requestContent: String): String {
        var substring = requestContent.substring(0, min(JSON_OUTPUT_LENGTH, requestContent.length))
        if (substring.length == JSON_OUTPUT_LENGTH) {
            substring += """
                    ...
                                        
                    /// JSON SHORTENED TO $JSON_OUTPUT_LENGTH SYMBOLS ///
                                        
                    
                                        
                    """.trimIndent()
        }
        return substring
    }

    protected fun extractPrettyParams(mvcResult: MvcResult): String {
        val parameterMap = mvcResult.request.parameterMap
        var stringParams = parameterMap.keys.stream()
            .map { "$it=${java.lang.String.join(",", *parameterMap[it])}" }
            .collect(Collectors.joining("&", "?", ""))
        if (stringParams == "?") {
            stringParams = ""
        }
        return stringParams
    }

    companion object {

        // todo: оставить тут только работу с парсингом
        const val JSON_OUTPUT_LENGTH = 2048
        val TEST_RESULTS_TEMPLATE =
            """
                     
                        
            /// TEST RESULTS ///
                        
            REQUEST: 
            %s %s%s -> %d
                        
            REQUEST JSON: 
            %s
                        
            RESPONSE JSON: 
            %s
            
            """.trimIndent()
    }
}
