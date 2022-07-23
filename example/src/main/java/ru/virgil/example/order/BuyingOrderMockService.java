package ru.virgil.example.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.virgil.example.user.UserDetails;
import ru.virgil.utils.FakerUtils;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BuyingOrderMockService {

    private final FakerUtils faker;
    private final BuyingOrderService service;

    public List<BuyingOrder> mock(UserDetails owner, int count) {
        List<BuyingOrder> entities = init(count);
        entities = entities.stream()
                .map(entity -> fill(owner, entity))
                .toList();
        return service.getRepository().saveAll(entities);
    }

    public BuyingOrder mock(UserDetails owner) {
        BuyingOrder entity = service.getRepository().save(new BuyingOrder());
        return service.getRepository().save(fill(owner, entity));
    }

    private List<BuyingOrder> init(int count) {
        List<BuyingOrder> entities = IntStream.range(0, count)
                .mapToObj(value -> new BuyingOrder())
                .toList();
        return service.getRepository().saveAll(entities);
    }

    private BuyingOrder fill(UserDetails owner, BuyingOrder entity) {
        entity.setOwner(owner);
        entity.setDescription(faker.chuckNorris().fact());
        return entity;
    }

}
