package ru.virgil.example.truck;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import ru.virgil.example.order.BuyingOrder;
import ru.virgil.example.user.UserDetails;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TruckMockService {

    private final Random random;
    private final Faker faker;
    private final TruckService service;

    public List<Truck> mock(UserDetails owner, int count) {
        List<Truck> entities = init(count);
        entities = entities.stream()
                .map(entity -> fill(owner, entity))
                .toList();
        return service.getRepository().saveAll(entities);
    }

    public Truck mock(UserDetails owner) {
        Truck entity = service.getRepository().save(new Truck());
        return service.getRepository().save(fill(owner, entity));
    }

    private List<Truck> init(int count) {
        List<Truck> entities = IntStream.range(0, count)
                .mapToObj(value -> new Truck())
                .toList();
        return service.getRepository().saveAll(entities);
    }

    private Truck fill(UserDetails owner, Truck entity) {
        return entity;
    }

    public List<Truck> bind(List<Truck> sources, List<BuyingOrder> bound) {
        sources = sources.stream()
                .peek(source -> {
                    BuyingOrder buyingOrder = bound.get(random.nextInt(bound.size()));
                    source.setBuyingOrder(buyingOrder);
                    buyingOrder.getTrucks().add(source);
                })
                .toList();
        return service.getRepository().saveAll(sources);
    }

}
