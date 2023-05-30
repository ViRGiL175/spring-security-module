package ru.virgil.spring_tools.examples.image

import org.springframework.stereotype.Repository
import ru.virgil.spring_tools.tools.image.PrivateImageRepositoryInterface

@Repository
interface PrivateImageRepository : PrivateImageRepositoryInterface<PrivateImageFile>
