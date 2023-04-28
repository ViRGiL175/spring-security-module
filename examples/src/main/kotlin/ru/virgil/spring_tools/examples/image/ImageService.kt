package ru.virgil.spring_tools.examples.image

import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import ru.virgil.spring_tools.examples.user.UserDetails
import ru.virgil.spring_tools.tools.util.image.FileTypeService
import java.nio.file.Path
import java.util.*

@Service
class ImageService(
    resourceLoader: ResourceLoader,
    privateImageRepository: PrivateImageRepository,
    fileTypeService: FileTypeService,
) : ru.virgil.spring_tools.tools.util.image.ImageService<UserDetails, PrivateImageFile>(
    resourceLoader,
    privateImageRepository,
    fileTypeService
) {

    override fun createPrivateImageFile(uuid: UUID, owner: UserDetails, imageFilePath: Path): PrivateImageFile {
        val privateImageFile = PrivateImageFile(owner, imageFilePath)
        privateImageFile.uuid = uuid
        return privateImageFile
    }
}
