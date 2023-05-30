package ru.virgil.spring_tools.examples.order


import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository
import ru.virgil.spring_tools.examples.system.entity.OwnedRepository
import ru.virgil.spring_tools.examples.truck.Truck
import java.util.*

@Repository
interface BuyingOrderRepository : OwnedRepository<BuyingOrder> {

    fun findAllByCreatedBy(createdBy: UserDetails, pageable: Pageable): List<BuyingOrder>
    fun findByCreatedByAndUuid(createdBy: UserDetails, uuid: UUID): Optional<BuyingOrder>
    fun countAllByCreatedBy(createdBy: UserDetails): Long
    fun findAllByTruck(truck: Truck, pageable: Pageable): List<BuyingOrder>
}
