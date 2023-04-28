package ru.virgil.spring_tools.examples.box

import jakarta.persistence.*
import ru.virgil.spring_tools.examples.system.entity.OwnedEntity
import ru.virgil.spring_tools.examples.truck.Truck
import ru.virgil.spring_tools.examples.user.UserDetails

@Entity
class Box(
    owner: UserDetails,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var type: BoxType = BoxType.USUAL,
    @ManyToOne(cascade = [CascadeType.REMOVE])
    var truck: Truck,
    var description: String,
    var price: Int = 0,
    var weight: Float = 0f,
) : OwnedEntity(owner)
