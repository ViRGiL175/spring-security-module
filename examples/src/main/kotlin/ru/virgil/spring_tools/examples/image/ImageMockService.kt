package ru.virgil.spring_tools.examples.image

import net.datafaker.Faker
import org.springframework.stereotype.Service
import ru.virgil.spring_tools.tools.image.ImageMockService
import ru.virgil.spring_tools.tools.image.ImageProperties
import ru.virgil.spring_tools.tools.image.ImageService

// TODO: Нужно ли?
@Service
class ImageMockService(
    imageService: ImageService<PrivateImageFile>,
    faker: Faker,
    imageProperties: ImageProperties,
) : ImageMockService<PrivateImageFile>(
    imageService,
    faker,
    imageProperties
)
