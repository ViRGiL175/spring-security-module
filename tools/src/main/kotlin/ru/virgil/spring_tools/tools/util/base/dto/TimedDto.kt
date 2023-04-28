package ru.virgil.spring_tools.tools.util.base.dto

import java.time.LocalDateTime

interface TimedDto {

    var createdAt: LocalDateTime?
    var updatedAt: LocalDateTime?

}
