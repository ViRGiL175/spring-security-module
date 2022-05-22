package ru.virgil.example.truck;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.virgil.example.order.BuyingOrder;
import ru.virgil.example.order.BuyingOrderService;
import ru.virgil.example.user.UserDetails;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TruckService {

    @Getter
    private final TruckRepository repository;
    private final BuyingOrderService buyingOrderService;

    public Truck get(UUID uuid) {
        return repository.findById(uuid).orElseThrow();
    }

    public List<Truck> getAll(BuyingOrder buyingOrder, int page, int size) {
        return repository.findAllByBuyingOrder(buyingOrder, PageRequest.of(page, size));
    }

    public long countMy(UserDetails owner) {
        List<BuyingOrder> buyingOrders = buyingOrderService.getAll(0, Integer.MAX_VALUE);
        Set<Truck> trucksSet = new HashSet<>();
        buyingOrders.forEach(buyingOrder -> {
            List<Truck> trucks = repository.findAllByBuyingOrder(buyingOrder, PageRequest.of(0, Integer.MAX_VALUE));
            trucksSet.addAll(trucks);
        });
        return trucksSet.size();
    }

    public Truck getRandom() {
        return repository.findAll().stream().findAny().orElseThrow();
    }

}
