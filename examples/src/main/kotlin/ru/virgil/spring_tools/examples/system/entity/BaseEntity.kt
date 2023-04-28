package ru.virgil.spring_tools.examples.system.entity

import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import ru.virgil.spring_tools.tools.util.base.entity.Timed.LocalDateTimeExtensions.databaseTruncate
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity(
    createdAt: LocalDateTime = LocalDateTime.now(),
    updatedAt: LocalDateTime = LocalDateTime.now(),
    var deleted: Boolean = false,
) {

    @UpdateTimestamp
    var updatedAt = updatedAt
        get() = field.databaseTruncate()
        set(value) {
            field = value.databaseTruncate()
        }

    @CreationTimestamp
    var createdAt = createdAt
        get() = field.databaseTruncate()
        set(value) {
            field = value.databaseTruncate()
        }

}
