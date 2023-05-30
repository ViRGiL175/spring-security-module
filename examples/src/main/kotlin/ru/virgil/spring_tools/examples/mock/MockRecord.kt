package ru.virgil.spring_tools.examples.mock

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import ru.virgil.spring_tools.examples.security.SecurityUser
import ru.virgil.spring_tools.tools.util.data.Identified
import ru.virgil.spring_tools.tools.util.data.Owned
import ru.virgil.spring_tools.tools.util.data.Timed
import java.time.LocalDateTime
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class MockRecord(
    @CreatedBy
    @ManyToOne
    override var createdBy: SecurityUser,
) : Owned, Identified, Timed {

    @Id
    @GeneratedValue
    override lateinit var uuid: UUID

    @CreationTimestamp
    override lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    override lateinit var updatedAt: LocalDateTime
}
