package ru.virgil.spring_tools.examples.image

import jakarta.persistence.Entity
import jakarta.persistence.Transient
import ru.virgil.spring_tools.examples.system.entity.OwnedEntity
import ru.virgil.spring_tools.examples.user.UserDetails
import ru.virgil.spring_tools.tools.util.image.PrivateImage
import java.nio.file.Path


@Entity
class PrivateImageFile(
    owner: UserDetails,
    fileLocation: Path,
) : OwnedEntity(owner), PrivateImage<UserDetails> {

    @get:Transient
    override var fileLocation: Path
        get() = Path.of(location)
        set(value) {
            location = value.toString()
        }

    private var location: String = fileLocation.toString()
}
