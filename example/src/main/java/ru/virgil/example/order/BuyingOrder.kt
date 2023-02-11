package ru.virgil.example.order

import ru.virgil.example.system.entity.OwnedEntity
import ru.virgil.example.truck.Truck
import ru.virgil.example.user.UserDetails
import javax.persistence.Entity
import javax.persistence.ManyToOne

/**
 * Это фикс поведения JPA для двусторонних связей. Проблема тянется еще с JPA 2,
 * но ее не фиксят, чтобы не сломать совместимость. Читал на StackOverflow
 */
private const val CONNECTION = "buying_order"

@Entity
class BuyingOrder(
    owner: UserDetails,
    @ManyToOne
    var truck: Truck,
    var description: String?,
) : OwnedEntity(owner)
