package ru.virgil.spring_tools.examples.mock

import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Lazy
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
import ru.virgil.spring_tools.examples.user.UserSettings
import ru.virgil.spring_tools.examples.user.UserSettingsMocker
import ru.virgil.spring_tools.examples.user.UserSettingsRepository
import java.util.*

@Component
class Mocker(
    private val mockRecordRepository: MockRecordRepository,
    @Lazy
    private val mockAuthSuccessHandler: MockAuthSuccessHandler,
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
    @Lazy
    @Qualifier(UserSettingsMocker.new)
    private val userSettingsMocker: ObjectProvider<UserSettings>,
    private val userSettingsRepository: UserSettingsRepository,
) : MockerUtils {

    fun start() {
        val principal = mockAuthSuccessHandler.principal
        val mockRecords = mockRecordRepository.findAllByCreatedBy(principal)
        if (mockRecords.isNotEmpty()) return
        mock(1, userSettingsMocker, userSettingsRepository)
        mock(10, truckMocker, truckRepository)
        mock(5, buyingOrderMocker, buyingOrderRepository)
        mock(10, boxMocker, boxRepository)
        // todo: во время мокирования createdBy выставляется вручную
        val mockRecord = MockRecord(principal)
        mockRecordRepository.save(mockRecord)
    }
}
