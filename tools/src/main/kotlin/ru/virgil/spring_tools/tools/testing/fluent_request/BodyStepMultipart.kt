package ru.virgil.spring_tools.tools.testing.fluent_request

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.mock.web.MockMultipartFile

class BodyStepMultipart(
    private val requestModel: RequestModel,
    private val requester: Requester,
    private val objectMapper: ObjectMapper,
) : BodyStepMultipartStart, BodyStep {

    private var byteArrayJavaType: JavaType? = null

    @Throws(Exception::class)
    override fun file(mockMultipartFile: MockMultipartFile): BodyStepMultipart {
        requestModel.mockMultipartFile = (mockMultipartFile)
        return this
    }

    fun receive(responseClass: Class<*>?, vararg responseClasses: Class<*>): BodyStepMultipart {
        val javaType = objectMapper.typeFactory.constructParametricType(responseClass, *responseClasses)
        requestModel.responseJavaType = (javaType)
        return this
    }

    fun receiveBytes(): BodyStepMultipart {
        byteArrayJavaType = objectMapper.typeFactory.constructParametricType(MutableList::class.java, Byte::class.java)
        requestModel.responseJavaType = (byteArrayJavaType)
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
