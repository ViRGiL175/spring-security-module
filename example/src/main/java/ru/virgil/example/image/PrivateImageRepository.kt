package ru.virgil.example.image

import org.springframework.stereotype.Repository
import ru.virgil.example.user.UserDetails
import ru.virgil.utils.image.IPrivateImageRepository

@Repository
interface PrivateImageRepository : IPrivateImageRepository<UserDetails, PrivateImageFile>
