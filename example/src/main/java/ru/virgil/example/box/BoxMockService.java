package ru.virgil.example.box;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.virgil.example.truck.Truck;
import ru.virgil.example.user.UserDetails;
import ru.virgil.utils.FakerUtils;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BoxMockService {

    private final BoxService service;
    private final Random random;
    private final FakerUtils faker;

    public List<Box> mock(UserDetails owner, int count) {
        List<Box> entities = init(count);
        entities = entities.stream()
                .map(entity -> fill(owner, entity))
                .toList();
        return service.getRepository().saveAll(entities);
    }

    public Box mock(UserDetails owner) {
        Box entity = service.getRepository().save(new Box());
        return service.getRepository().save(fill(owner, entity));
    }

    private List<Box> init(int count) {
        List<Box> entities = IntStream.range(0, count)
                .mapToObj(value -> new Box())
                .toList();
        return service.getRepository().saveAll(entities);
    }

    private Box fill(UserDetails owner, Box entity) {
        entity.setOwner(owner);
        entity.setDescription(faker.chuckNorris().fact());
        entity.setPrice(random.nextInt(100, 10000));
        entity.setWeight(random.nextFloat(15f, 1000f));
        return entity;
    }

    public List<Box> bind(List<Box> sources, List<Truck> bound) {
        sources = sources.stream()
                .peek(source -> {
                    Truck truck = bound.get(random.nextInt(bound.size()));
                    source.setTruck(truck);
                    truck.getBoxes().add(source);
                })
                .toList();
        return service.getRepository().saveAll(sources);
    }

}
