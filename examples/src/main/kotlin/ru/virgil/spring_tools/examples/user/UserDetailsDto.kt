package ru.virgil.spring_tools.examples.user

import ru.virgil.spring_tools.examples.system.dto.IdentifiedDto
import java.time.LocalDateTime
import java.util.*

class UserDetailsDto(
    override var createdAt: LocalDateTime?,
    override var updatedAt: LocalDateTime?,
    override var uuid: UUID?,
    var name: String? = null,
) : IdentifiedDto
