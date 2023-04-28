package ru.virgil.spring_tools.examples.system.entity

import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.util.*

// TODO: Переместить в базовый модуль
@MappedSuperclass
abstract class IdentifiedEntity(
    @Id
    @GeneratedValue
    var uuid: UUID = UUID.randomUUID(),
) : BaseEntity()
