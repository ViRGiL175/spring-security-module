package ru.virgil.spring_tools.examples.system.mock

import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Lazy
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import ru.virgil.spring_tools.examples.box.Box
import ru.virgil.spring_tools.examples.box.BoxMocker
import ru.virgil.spring_tools.examples.box.BoxRepository
import ru.virgil.spring_tools.examples.order.BuyingOrder
import ru.virgil.spring_tools.examples.order.BuyingOrderMocker
import ru.virgil.spring_tools.examples.order.BuyingOrderRepository
import ru.virgil.spring_tools.examples.truck.Truck
import ru.virgil.spring_tools.examples.truck.TruckMocker
import ru.virgil.spring_tools.examples.truck.TruckRepository
import java.util.*

@Component
class Mocker(
    @Lazy
    @Qualifier(BuyingOrderMocker.new)
    private val buyingOrderMocker: ObjectProvider<BuyingOrder>,
    private val buyingOrderRepository: BuyingOrderRepository,
    @Lazy
    @Qualifier(BoxMocker.new)
    private val boxMocker: ObjectProvider<Box>,
    private val boxRepository: BoxRepository,
    @Lazy
    @Qualifier(TruckMocker.new)
    private val truckMocker: ObjectProvider<Truck>,
    private val truckRepository: TruckRepository,
) {

    fun mock() {
        mock(10, truckMocker, truckRepository)
        mock(5, buyingOrderMocker, buyingOrderRepository)
        mock(10, boxMocker, boxRepository)
    }

    protected fun <Entity> mock(
        number: Int,
        objectProvider: ObjectProvider<Entity>,
        repository: CrudRepository<Entity, UUID>,
    ) {
        val chats = ArrayList<Entity>()
        for (i in 1..number) chats.add(objectProvider.getObject())
        repository.saveAll(chats)
    }

}
