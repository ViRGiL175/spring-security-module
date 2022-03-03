package ru.virgil.example.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.virgil.example.truck.Truck;
import ru.virgil.example.truck.TruckDto;
import ru.virgil.example.truck.TruckMapper;
import ru.virgil.example.truck.TruckService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/buying_order")
@RequiredArgsConstructor
public class BuyingOrderController {

    public static final String PAGE_PARAM = "page";
    public static final String PAGE_SIZE_PARAM = "size";
    private final BuyingOrderService buyingOrderService;
    private final BuyingOrderMapper buyingOrderMapper;
    private final TruckService truckService;
    private final TruckMapper truckMapper;

    @GetMapping
    public List<BuyingOrderDto> getAll(@RequestParam(PAGE_PARAM) int page, @RequestParam(PAGE_SIZE_PARAM) int size) {
        return buyingOrderService.getAll(page, size).stream()
                .map(buyingOrderMapper::toDto)
                .toList();
    }

    @GetMapping("/{uuid}")
    public BuyingOrderDto get(@PathVariable UUID uuid) {
        BuyingOrder buyingOrder = buyingOrderService.get(uuid);
        return buyingOrderMapper.toDto(buyingOrder);
    }

    @GetMapping("/{buyingOrderUuid}/truck")
    public List<TruckDto> getTrucksByOrder(@PathVariable UUID buyingOrderUuid, @RequestParam(PAGE_PARAM) int page,
            @RequestParam(PAGE_SIZE_PARAM) int size) {
        BuyingOrder buyingOrder = buyingOrderService.get(buyingOrderUuid);
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
