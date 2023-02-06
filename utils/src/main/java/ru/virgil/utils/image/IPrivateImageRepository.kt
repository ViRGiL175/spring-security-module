package ru.virgil.utils.image

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

@NoRepositoryBean
interface IPrivateImageRepository<Owner : IBaseEntity, PrivateImage : IPrivateImage<Owner>> :
    CrudRepository<PrivateImage, UUID> {

    fun findByOwnerAndUuid(owner: Owner, imageUuid: UUID): Optional<PrivateImage>
}
