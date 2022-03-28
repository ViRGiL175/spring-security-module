package ru.virgil.example.box;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.virgil.example.truck.Truck;
import ru.virgil.example.truck.TruckService;
import ru.virgil.example.user.UserDetails;
import ru.virgil.example.user.UserDetailsService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoxService {

    @Getter
    private final BoxRepository repository;
    private final UserDetailsService userDetailsService;
    private final TruckService truckService;
    private final BoxMerger boxMerger;

    public List<Box> getAll(int page, int size) {
        return repository.findAllByOwner(getOwner(), PageRequest.of(page, size));
    }

    public List<Box> getAll(Truck truck, int page, int size) {
        return repository.findAllByOwnerAndTruck(getOwner(), truck,
                PageRequest.of(page, size));
    }

    public Box get(UUID uuid) {
        return repository.findByOwnerAndUuid(getOwner(), uuid).orElseThrow();
    }

    public Box create(Box box) {
        Truck truck = truckService.getRandom();
        box.setTruck(truck);
        box.setOwner(getOwner());
        return repository.save(box);
    }

    public Box edit(UUID uuid, Box patchBox) {
        Box serverBox = repository.findById(uuid).orElseThrow();
        serverBox = boxMerger.merge(serverBox, patchBox);
        return repository.save(serverBox);
    }

    public void delete(UUID uuid) {
        Box box = get(uuid);
        repository.delete(box);
    }

    public List<Box> getAllWeaponed() {
        return repository.findAllByType(BoxType.WEAPONED);
    }

    public long countMyBoxes() {
        return repository.countAllByOwner(getOwner());
    }

    private UserDetails getOwner() {
        return userDetailsService.getCurrentUser();
    }

}
