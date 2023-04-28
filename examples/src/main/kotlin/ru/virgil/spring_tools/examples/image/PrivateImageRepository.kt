package ru.virgil.spring_tools.examples.image

import org.springframework.stereotype.Repository
import ru.virgil.spring_tools.examples.user.UserDetails

@Repository
interface PrivateImageRepository :
    ru.virgil.spring_tools.tools.util.image.PrivateImageRepository<UserDetails, PrivateImageFile>
