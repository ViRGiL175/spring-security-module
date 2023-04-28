package ru.virgil.spring_tools.examples.truck

import ru.virgil.spring_tools.examples.system.dto.IdentifiedDto
import java.time.LocalDateTime
import java.util.*

data class TruckDto(
    override var createdAt: LocalDateTime?,
    override var updatedAt: LocalDateTime?,
    override var uuid: UUID?,
    var boxesCount: Int?,
) : IdentifiedDto
