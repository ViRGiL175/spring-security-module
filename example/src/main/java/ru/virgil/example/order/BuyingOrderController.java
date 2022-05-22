package ru.virgil.example.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.virgil.example.system.HttpAddressConstants;
import ru.virgil.example.truck.Truck;
import ru.virgil.example.truck.TruckDto;
import ru.virgil.example.truck.TruckMapper;
import ru.virgil.example.truck.TruckService;
import ru.virgil.example.user.UserDetails;
import ru.virgil.example.user.UserDetailsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/buying_order")
@RequiredArgsConstructor
public class BuyingOrderController implements HttpAddressConstants {

    private final UserDetailsService userDetailsService;
    private final BuyingOrderService buyingOrderService;
    private final BuyingOrderMapper buyingOrderMapper;
    private final TruckService truckService;
    private final TruckMapper truckMapper;

    @GetMapping
    public List<BuyingOrderDto> getAll(@RequestParam(PAGE_PARAM) int page, @RequestParam(PAGE_SIZE_PARAM) int size) {
        UserDetails owner = userDetailsService.getCurrentUser();
        return buyingOrderService.getAll(owner, page, size).stream()
                .map(buyingOrderMapper::toDto)
                .toList();
    }

    @GetMapping("/{uuid}")
    public BuyingOrderDto get(@PathVariable UUID uuid) {
        UserDetails owner = userDetailsService.getCurrentUser();
        BuyingOrder buyingOrder = buyingOrderService.get(owner, uuid);
        return buyingOrderMapper.toDto(buyingOrder);
    }

    @GetMapping("/{buyingOrderUuid}/truck")
    public List<TruckDto> getTrucksByOrder(@PathVariable UUID buyingOrderUuid, @RequestParam(PAGE_PARAM) int page,
            @RequestParam(PAGE_SIZE_PARAM) int size) {
        UserDetails owner = userDetailsService.getCurrentUser();
        BuyingOrder buyingOrder = buyingOrderService.get(owner, buyingOrderUuid);
        List<Truck> trucks = truckService.getAll(buyingOrder, page, size);
        return trucks.stream()
                .map(truck -> {
                    // todo: сложный маппинг?
                    TruckDto truckDto = truckMapper.toDto(truck);
                    truckDto.setBoxesCount(truck.getBoxes().size());
                    return truckDto;
                })
                .toList();
    }

}
