package ru.virgil.spring_tools.examples.order

import ru.virgil.spring_tools.examples.system.dto.IdentifiedDto
import java.time.LocalDateTime
import java.util.*

class BuyingOrderDto(
    override var createdAt: LocalDateTime?,
    override var updatedAt: LocalDateTime?,
    override var uuid: UUID?,
    var description: String?,
) : IdentifiedDto
