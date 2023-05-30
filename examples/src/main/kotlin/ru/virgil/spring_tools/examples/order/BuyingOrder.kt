package ru.virgil.spring_tools.examples.order

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import ru.virgil.spring_tools.examples.security.SecurityUser
import ru.virgil.spring_tools.examples.truck.Truck
import ru.virgil.spring_tools.tools.util.data.Identified
import ru.virgil.spring_tools.tools.util.data.Owned
import ru.virgil.spring_tools.tools.util.data.Soft
import ru.virgil.spring_tools.tools.util.data.Timed
import java.time.LocalDateTime
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class BuyingOrder(
    @ManyToOne
    var truck: Truck,
    var description: String?,
) : Owned, Identified, Timed, Soft {

    @Id
    @GeneratedValue
    override lateinit var uuid: UUID

    @CreationTimestamp
    override lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    override lateinit var updatedAt: LocalDateTime

    @CreatedBy
    @ManyToOne
    override lateinit var createdBy: SecurityUser

    override var deleted: Boolean = false
}
