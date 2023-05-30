package ru.virgil.spring_tools.examples.system.dto

import java.time.LocalDateTime

interface TimedDto {

    var createdAt: LocalDateTime?
    var updatedAt: LocalDateTime?
}
