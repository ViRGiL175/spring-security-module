package ru.virgil.spring_tools.examples.system.dto

import java.time.LocalDateTime

interface Dto {

    var createdAt: LocalDateTime?
    var updatedAt: LocalDateTime?
}
