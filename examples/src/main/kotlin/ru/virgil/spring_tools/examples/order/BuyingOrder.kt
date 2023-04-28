package ru.virgil.spring_tools.examples.order

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import ru.virgil.spring_tools.examples.system.entity.OwnedEntity
import ru.virgil.spring_tools.examples.truck.Truck
import ru.virgil.spring_tools.examples.user.UserDetails

@Entity
class BuyingOrder(
    owner: UserDetails,
    @ManyToOne
    var truck: Truck,
    var description: String?,
) : OwnedEntity(owner)
