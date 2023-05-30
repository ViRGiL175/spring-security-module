package ru.virgil.spring_tools.tools.image

import org.apache.tika.Tika
import org.springframework.stereotype.Component

@Component
open class FileTypeService : Tika() {

    fun getImageMimeType(content: ByteArray): String {
        val mimeType = detect(content)
        val imageTypeRegex = "image/.*"
        if (!mimeType.matches(imageTypeRegex.toRegex())) {
            throw UnsupportedOperationException("File should be an image ($imageTypeRegex). Given file: $mimeType")
        }
        return mimeType
    }
}