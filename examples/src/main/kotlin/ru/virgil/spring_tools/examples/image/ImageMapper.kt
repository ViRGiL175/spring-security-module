package ru.virgil.spring_tools.examples.image

import kotlin.io.path.name

interface ImageMapper {

    fun PrivateImageFile.toDto(): PrivateImageFileDto =
        PrivateImageFileDto(uuid, createdAt, updatedAt, this.fileLocation.name)
}
