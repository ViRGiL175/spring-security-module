package ru.virgil.utils.image

import java.time.LocalDateTime
import java.util.*

interface IBaseEntity {

    var uuid: UUID?
    var createdAt: LocalDateTime?
    var updatedAt: LocalDateTime?
}
