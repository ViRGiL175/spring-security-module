package ru.virgil.spring_tools.examples.image



import ru.virgil.spring_tools.examples.system.dto.IdentifiedDto
import java.time.LocalDateTime
import java.util.*

data class PrivateImageFileDto(
    override var uuid: UUID?,
    override var createdAt: LocalDateTime?,
    override var updatedAt: LocalDateTime?,
    var name: String?,
) : IdentifiedDto
