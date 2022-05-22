package ru.virgil.example.box;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.virgil.example.truck.Truck;
import ru.virgil.example.user.UserDetails;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoxService {

    @Getter
    private final BoxRepository repository;
    private final BoxMerger boxMerger;

    public List<Box> getAll(UserDetails owner, int page, int size) {
        return repository.findAllByOwner(owner, PageRequest.of(page, size));
    }

    public List<Box> getAll(UserDetails owner, Truck truck, int page, int size) {
        return repository.findAllByOwnerAndTruck(owner, truck, PageRequest.of(page, size));
    }

    public Box get(UserDetails owner, UUID uuid) {
        return repository.findByOwnerAndUuid(owner, uuid).orElseThrow();
    }

    public Box create(UserDetails owner, Truck truck, Box box) {
        box.setTruck(truck);
        box.setOwner(owner);
        return repository.save(box);
    }

    public Box edit(UserDetails owner, UUID uuid, Box patchBox) {
        Box serverBox = repository.findById(uuid).orElseThrow();
        serverBox = boxMerger.merge(serverBox, patchBox);
        return repository.save(serverBox);
    }

    public void delete(UserDetails owner, UUID uuid) {
        Box box = get(owner, uuid);
        repository.delete(box);
    }

    public List<Box> getAllWeapons(UserDetails owner) {
        return repository.findAllByOwnerAndType(owner, BoxType.WEAPON);
    }

    public long countMyBoxes(UserDetails owner) {
        return repository.countAllByOwner(owner);
    }

}
