package ru.virgil.spring_tools.tools.util.image

import jakarta.annotation.PreDestroy
import net.datafaker.Faker
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.mock.web.MockMultipartFile
import ru.virgil.spring_tools.tools.util.base.entity.Identified
import java.io.BufferedInputStream
import java.io.IOException
import java.net.URL


@EnableConfigurationProperties(ImageProperties::class)
abstract class ImageMockService<Owner : Identified, Image : PrivateImage<Owner>>(
    @Suppress("MemberVisibilityCanBePrivate")
    protected val imageService: ImageService<Owner, Image>,
    @Suppress("MemberVisibilityCanBePrivate")
    protected val fakerUtils: Faker,
    @Suppress("MemberVisibilityCanBePrivate")
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
