package ru.virgil.spring_tools.examples.order

import org.springframework.web.bind.annotation.*
import ru.virgil.spring_tools.examples.system.rest.RestValues.pageParam
import ru.virgil.spring_tools.examples.system.rest.RestValues.pageSizeParam
import ru.virgil.spring_tools.examples.truck.TruckDto
import ru.virgil.spring_tools.examples.truck.TruckMapper
import ru.virgil.spring_tools.examples.truck.TruckService
import java.util.*

@RestController
@RequestMapping("/buying_order")
class BuyingOrderController(
    private val buyingOrderService: BuyingOrderService,
    private val truckService: TruckService,
) : BuyingOrderMapper, TruckMapper {

    @GetMapping
    fun getAll(@RequestParam(pageParam) page: Int, @RequestParam(pageSizeParam) size: Int): List<BuyingOrderDto> =
        buyingOrderService.getAll(page, size).stream()
            .map { it.toDto() }
            .toList()

    @GetMapping("/{uuid}")
    operator fun get(@PathVariable uuid: UUID): BuyingOrderDto {
        val buyingOrder = buyingOrderService.get(uuid)
        return buyingOrder.toDto()
    }

    @GetMapping("/{buyingOrderUuid}/truck")
    fun getTrucksByOrder(
        @PathVariable buyingOrderUuid: UUID,
        @RequestParam(pageParam) page: Int,
        @RequestParam(pageSizeParam) size: Int,
    ): List<TruckDto> {
        val buyingOrder = buyingOrderService.get(buyingOrderUuid)
        val trucks = truckService.getAll(buyingOrder, page, size)
        return trucks.stream()
            .map { it.toDto() }
            .toList()
    }
}
