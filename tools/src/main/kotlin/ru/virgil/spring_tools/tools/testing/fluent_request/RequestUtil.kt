package ru.virgil.spring_tools.tools.testing.fluent_request

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Deprecated("Use Fluent requests", ReplaceWith("Fluent"))
@Component
class RequestUtil(
    private val requester: Requester,
    private val objectMapper: ObjectMapper,
) {

    private fun startJsonBuilding(requestMethod: RequestMethod, path: String): BodyStepJson {
        val requestModel = RequestModel()
        requestModel.requestMethod = requestMethod
        requestModel.url = path
        return BodyStepJson(requestModel, requester, objectMapper)
    }

    private fun startMultipartBuilding(path: String): BodyStepMultipartStart {
        val requestModel = RequestModel()
        requestModel.requestMethod = RequestMethod.POST_MULTIPART
        requestModel.url = path
        return BodyStepMultipart(requestModel, requester, objectMapper)
    }

    fun get(path: String): BodyStepJsonStart = startJsonBuilding(RequestMethod.GET, path)

    fun post(path: String): BodyStepJsonStart = startJsonBuilding(RequestMethod.POST, path)

    fun put(path: String): BodyStepJsonStart = startJsonBuilding(RequestMethod.PUT, path)

    fun delete(path: String): BodyStepJsonStart = startJsonBuilding(RequestMethod.DELETE, path)

    fun postMultipart(path: String): BodyStepMultipartStart = startMultipartBuilding(path)

}
