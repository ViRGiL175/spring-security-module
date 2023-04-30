package ru.virgil.spring_tools.examples.image

import org.apache.commons.io.IOUtils
import org.springframework.core.io.FileSystemResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.virgil.spring_tools.examples.user.UserDetailsService
import ru.virgil.spring_tools.tools.util.image.FileTypeService
import java.io.IOException
import java.nio.file.Paths
import java.util.*

@RestController
@RequestMapping("/image")
class ImageController(
    private val imageService: ImageService,
    private val userDetailsService: UserDetailsService,
    private val fileTypeService: FileTypeService,
) : ImageMapper {

    @GetMapping("/public/{imageName}")
    fun getPublic(@PathVariable imageName: String): ResponseEntity<ByteArray> {
        val filePath = Paths.get(imageService.getPublic(imageName).uri)
        val imageBytes = IOUtils.toByteArray(FileSystemResource(filePath).inputStream)
        val imageMime = fileTypeService.getImageMimeType(imageBytes)
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageMime)).body(imageBytes)
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("/protected/{imageName}")
    fun getProtected(@PathVariable imageName: String): ResponseEntity<ByteArray> {
        val filePath = Paths.get(imageService.getProtected(imageName).uri)
        val imageBytes = IOUtils.toByteArray(FileSystemResource(filePath).inputStream)
        val imageMime = fileTypeService.getImageMimeType(imageBytes)
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageMime)).body(imageBytes)
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("/private/{imageUuid}")
    fun getPrivate(@PathVariable imageUuid: UUID): ResponseEntity<ByteArray> {
        val currentUser = userDetailsService.getCurrentUser()
        val filePath = Paths.get(imageService.getPrivate(currentUser, imageUuid).uri)
        val imageBytes = IOUtils.toByteArray(FileSystemResource(filePath).inputStream)
        val imageMime = fileTypeService.getImageMimeType(imageBytes)
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageMime)).body(imageBytes)
    }

    @PreAuthorize("isFullyAuthenticated()")
    @PostMapping("/private")
    fun postPrivate(
        @RequestParam image: MultipartFile,
        @RequestParam(required = false) imageName: String?,
    ): PrivateImageFileDto {
        val currentUser = userDetailsService.getCurrentUser()
        val privateFileImage = imageService.savePrivate(image.bytes, imageName ?: "static/image", currentUser)
        return privateFileImage.toDto()
    }
}
