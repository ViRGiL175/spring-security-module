package ru.virgil.spring_tools.examples.system.dto

import java.util.*

interface IdentifiedDto : TimedDto {

    var uuid: UUID?
}
