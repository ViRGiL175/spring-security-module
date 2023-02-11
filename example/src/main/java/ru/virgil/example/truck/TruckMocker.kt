package ru.virgil.example.truck

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import ru.virgil.example.box.BoxRepository
import ru.virgil.example.order.BuyingOrderRepository
import ru.virgil.example.system.mock.BaseMocker
import java.util.*

@Lazy
@Component
class TruckMocker(
    private val buyingOrderRepository: BuyingOrderRepository,
    private val boxRepository: BoxRepository,
    private val truckRepository: TruckRepository,
) : BaseMocker() {

    companion object {

        private const val name = "truck"
        const val new = "new-$name"
        const val random = "random-$name"
    }

    @Bean(new)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun new(): Truck {
        // val buyingOrder = buyingOrderRepository.findPriorityOrGetRandom { it.trucks.isEmpty() }
        // var boxes = boxRepository.findAll().shuffled()
        // boxes = boxes.take(Random().nextInt(boxes.size))
        return Truck()
    }

    @Bean(random)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun random(): Truck = truckRepository.findAll().random()
}
