package ru.virgil.example.truck;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.virgil.example.order.BuyingOrder;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TruckService {

    @Getter
    private final TruckRepository repository;

    public Truck get(UUID uuid) {
        return repository.findById(uuid).orElseThrow();
    }

    public List<Truck> getAll(BuyingOrder buyingOrder, int page, int size) {
        return repository.findAllByBuyingOrder(buyingOrder, PageRequest.of(page, size));
    }

    public Truck assignTruck() {
        return repository.findAll().stream().findAny().orElseThrow();
    }

}
