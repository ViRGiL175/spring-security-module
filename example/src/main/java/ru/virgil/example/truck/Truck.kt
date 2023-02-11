package ru.virgil.example.truck

import ru.virgil.example.box.Box
import ru.virgil.example.order.BuyingOrder
import ru.virgil.example.system.entity.IdentifiedEntity
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany

private const val CONNECTION = "truck"

@Entity
class Truck(
    @OneToMany(cascade = [CascadeType.REMOVE], fetch = FetchType.EAGER, mappedBy = CONNECTION)
    var boxes: Set<Box> = HashSet(),
    @OneToMany(mappedBy = CONNECTION)
    val buyingOrder: Set<BuyingOrder> = HashSet(),
) : IdentifiedEntity()
