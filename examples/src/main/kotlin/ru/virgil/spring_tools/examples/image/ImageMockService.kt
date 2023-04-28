package ru.virgil.spring_tools.examples.image

import net.datafaker.Faker
import org.springframework.stereotype.Service
import ru.virgil.spring_tools.examples.user.UserDetails
import ru.virgil.spring_tools.tools.util.image.ImageProperties

// TODO: Нужно ли?
@Service
class ImageMockService(
    imageService: ru.virgil.spring_tools.tools.util.image.ImageService<UserDetails, PrivateImageFile>,
    faker: Faker,
    imageProperties: ImageProperties,
) : ru.virgil.spring_tools.tools.util.image.ImageMockService<UserDetails, PrivateImageFile>(
    imageService,
    faker,
    imageProperties
)
