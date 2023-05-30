package ru.virgil.spring_tools.examples.image

import org.springframework.core.io.ResourceLoader
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import ru.virgil.spring_tools.tools.image.FileTypeService
import ru.virgil.spring_tools.tools.image.ImageService
import java.nio.file.Path
import java.util.*

@Service
class ImageService(
    resourceLoader: ResourceLoader,
    privateImageRepository: PrivateImageRepository,
    fileTypeService: FileTypeService,
) : ImageService<PrivateImageFile>(
    resourceLoader,
    privateImageRepository,
    fileTypeService
) {

    override fun createPrivateImageFile(uuid: UUID, owner: UserDetails, imageFilePath: Path): PrivateImageFile {
        val privateImageFile = PrivateImageFile(imageFilePath)
        privateImageFile.uuid = uuid
        return privateImageFile
    }
}
