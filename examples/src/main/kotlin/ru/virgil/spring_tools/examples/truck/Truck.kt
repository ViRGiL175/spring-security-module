package ru.virgil.spring_tools.examples.truck

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import ru.virgil.spring_tools.examples.box.Box
import ru.virgil.spring_tools.examples.order.BuyingOrder
import ru.virgil.spring_tools.tools.util.data.Identified
import ru.virgil.spring_tools.tools.util.data.Soft
import ru.virgil.spring_tools.tools.util.data.Timed
import java.time.LocalDateTime
import java.util.*

/**
 * Это фикс поведения JPA для двусторонних связей. Проблема тянется еще с JPA 2,
 * но ее не фиксят, чтобы не сломать совместимость. Читал на StackOverflow
 */
private const val CONNECTION = "truck"

@Entity
@EntityListeners(AuditingEntityListener::class)
class Truck(
    @OneToMany(cascade = [CascadeType.REMOVE], fetch = FetchType.EAGER, mappedBy = CONNECTION)
    var boxes: Set<Box> = HashSet(),
    @OneToMany(mappedBy = CONNECTION, fetch = FetchType.EAGER)
    val buyingOrder: Set<BuyingOrder> = HashSet(),
) : Identified, Timed, Soft {

    @Id
    @GeneratedValue
    override lateinit var uuid: UUID

    @CreationTimestamp
    override lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    override lateinit var updatedAt: LocalDateTime

    override var deleted: Boolean = false
}
