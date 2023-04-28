package ru.virgil.spring_tools.examples.system.entity

import jakarta.persistence.ManyToOne
import jakarta.persistence.MappedSuperclass
import ru.virgil.spring_tools.examples.user.UserDetails

// TODO: Переместить в базовый модуль в виде интерфейса
@MappedSuperclass
abstract class OwnedEntity(
    @ManyToOne
    var owner: UserDetails,
) : IdentifiedEntity()
