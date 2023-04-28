package ru.virgil.spring_tools.tools.testing.fluent_request

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper


class BodyStepJson(
    private val requestModel: RequestModel,
    private val requester: Requester,
    private val objectMapper: ObjectMapper,
) : BodyStepJsonStart, BodyStep {

    private var byteArrayJavaType: JavaType? = null

    override fun send(dto: Any): BodyStepJson {
        requestModel.requestBody = dto
        return this
    }

    override fun receive(responseClass: Class<*>, vararg responseClasses: Class<*>): BodyStepJson {
        val javaType = objectMapper.typeFactory.constructParametricType(responseClass, *responseClasses)
        requestModel.responseJavaType = (javaType)
        return this
    }

    override fun receiveAsBytes(): BodyStepJson {
        byteArrayJavaType =
            objectMapper.typeFactory.constructParametricType(MutableList::class.java, Byte::class.java)
        requestModel.responseJavaType = (byteArrayJavaType)
        return this
    }

    override fun exchange(dto: Any, responseClass: Class<*>, vararg responseClasses: Class<*>): BodyStepJson {
        requestModel.requestBody = (dto)
        val javaType = objectMapper.typeFactory.constructParametricType(responseClass, *responseClasses)
        requestModel.responseJavaType = (javaType)
        return this
    }

    @Throws(Exception::class)
    override fun and(): FinalStep<*> {
        return if (requestModel.responseJavaType == null) {
            FinalStepNoType(requestModel, requester)
        } else {
            if (requestModel.responseJavaType == byteArrayJavaType) {
                FinalStepByteType(requestModel, requester)
            } else {
                FinalStepJsonType<Any>(requestModel, requester, objectMapper)
            }
        }
    }
}
