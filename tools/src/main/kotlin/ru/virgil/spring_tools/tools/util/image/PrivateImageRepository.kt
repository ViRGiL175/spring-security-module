package ru.virgil.spring_tools.tools.util.image

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean
import ru.virgil.spring_tools.tools.util.base.entity.Identified
import java.util.*

@NoRepositoryBean
interface PrivateImageRepository<Owner : Identified, PrivateImage : ru.virgil.spring_tools.tools.util.image.PrivateImage<Owner>> :
    CrudRepository<PrivateImage, UUID> {

    fun findByOwnerAndUuid(owner: Owner, imageUuid: UUID): Optional<PrivateImage>

}
