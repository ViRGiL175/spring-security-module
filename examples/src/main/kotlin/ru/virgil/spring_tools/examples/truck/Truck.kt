package ru.virgil.spring_tools.examples.truck

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import ru.virgil.spring_tools.examples.box.Box
import ru.virgil.spring_tools.examples.order.BuyingOrder
import ru.virgil.spring_tools.examples.system.entity.IdentifiedEntity

/**
 * Это фикс поведения JPA для двусторонних связей. Проблема тянется еще с JPA 2,
 * но ее не фиксят, чтобы не сломать совместимость. Читал на StackOverflow
 */
private const val CONNECTION = "truck"

@Entity
class Truck(
    @OneToMany(cascade = [CascadeType.REMOVE], fetch = FetchType.EAGER, mappedBy = CONNECTION)
    var boxes: Set<Box> = HashSet(),
    @OneToMany(mappedBy = CONNECTION, fetch = FetchType.EAGER)
    val buyingOrder: Set<BuyingOrder> = HashSet(),
) : IdentifiedEntity()
