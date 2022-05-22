package ru.virgil.example.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.virgil.example.box.BoxService;
import ru.virgil.example.order.BuyingOrderService;
import ru.virgil.example.truck.TruckCounterModule;
import ru.virgil.example.truck.TruckService;
import ru.virgil.example.user.UserDetails;
import ru.virgil.example.user.UserDetailsService;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final BoxService boxService;
    private final TruckService truckService;
    private final TruckCounterModule truckCounterModule;
    private final BuyingOrderService buyingOrderService;
    private final UserDetailsService userDetailsService;

    @GetMapping("/all")
    public StatsDto getAllStats() {
        long boxesCount = boxService.getRepository().count();
        long trucksCount = truckService.getRepository().count();
        long ordersCount = buyingOrderService.getRepository().count();
        return new StatsDto(boxesCount, trucksCount, ordersCount);
    }

    @GetMapping("/my")
    public StatsDto getMyStats() {
        UserDetails owner = userDetailsService.getCurrentUser();
        long boxesCount = boxService.countMyBoxes(owner);
        long trucksCount = truckCounterModule.countAll(owner);
        long ordersCount = buyingOrderService.countMy(owner);
        return new StatsDto(boxesCount, trucksCount, ordersCount);
    }
}
