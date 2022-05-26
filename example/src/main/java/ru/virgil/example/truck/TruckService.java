package ru.virgil.example.truck;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.virgil.example.order.BuyingOrder;
import ru.virgil.example.system.SimpleJpaAccess;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TruckService implements SimpleJpaAccess<Truck, UUID> {

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

    @Override
    public JpaRepository<Truck, UUID> getRepository() {
        return repository;
    }
}
