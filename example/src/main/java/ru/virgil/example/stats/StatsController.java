package ru.virgil.example.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.virgil.example.box.BoxService;
import ru.virgil.example.order.BuyingOrderService;
import ru.virgil.example.truck.TruckService;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final BoxService boxService;
    private final TruckService truckService;
    private final BuyingOrderService buyingOrderService;

    @GetMapping("/all")
    public StatsDto getAllStats() {
        long boxesCount = boxService.getRepository().count();
        long trucksCount = truckService.getRepository().count();
        long ordersCount = buyingOrderService.getRepository().count();
        return new StatsDto(boxesCount, trucksCount, ordersCount);
    }

    @GetMapping("/my")
    public StatsDto getMyStats() {
        long boxesCount = boxService.countMyBoxes();
        long trucksCount = truckService.countMy();
        long ordersCount = buyingOrderService.countMy();
        return new StatsDto(boxesCount, trucksCount, ordersCount);
    }

    public record StatsDto(long boxes, long trucks, long orders) {

    }

}
