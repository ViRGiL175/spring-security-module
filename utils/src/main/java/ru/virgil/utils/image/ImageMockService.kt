package ru.virgil.utils.image

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.mock.web.MockMultipartFile
import ru.virgil.utils.Faker
import java.io.BufferedInputStream
import java.io.IOException
import java.net.URL
import javax.annotation.PreDestroy

@EnableConfigurationProperties(ImageProperties::class)
abstract class ImageMockService<Owner : IBaseEntity, Image : IPrivateImage<Owner>>(
    protected val imageService: ImageService<Owner, Image>,
    protected val fakerUtils: Faker,
    protected val imageProperties: ImageProperties,
) {

    @Throws(ImageException::class)
    fun mockImage(owner: Owner): Image = try {
        imageService.savePrivate(mockAsMultipart().bytes, IMAGE_NAME, owner)
    } catch (e: IOException) {
        throw ImageException(e)
    }

    @Throws(ImageException::class)
    fun mockAsMultipart(): MockMultipartFile = try {
        val bufferedInputStream = BufferedInputStream(URL(fakerUtils.avatar().image()).openStream())
        MockMultipartFile(IMAGE_NAME, bufferedInputStream)
    } catch (e: IOException) {
        throw ImageException(e)
    }

    @PreDestroy
    @Throws(IOException::class)
    fun preDestroy() {
        if (imageProperties.cleanOnDestroy) {
            imageService.cleanFolders()
        }
    }

    companion object {

        const val IMAGE_NAME = "image"
    }
}
