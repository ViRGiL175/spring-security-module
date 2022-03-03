package ru.virgil.example.truck;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.virgil.example.order.BuyingOrder;

import java.util.List;
import java.util.UUID;

@Repository
public interface TruckRepository extends JpaRepository<Truck, UUID> {

    List<Truck> findAllByBuyingOrder(BuyingOrder buyingOrder, Pageable pageable);

}
