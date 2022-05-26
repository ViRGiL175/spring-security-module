package ru.virgil.example.order;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.virgil.example.system.SimpleJpaAccess;
import ru.virgil.example.user.UserDetails;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuyingOrderService implements SimpleJpaAccess<BuyingOrder, UUID> {

    private final BuyingOrderRepository repository;

    public List<BuyingOrder> getAll(UserDetails owner, int page, int size) {
        return repository.findAllByOwner(owner, PageRequest.of(page, size));
    }

    public BuyingOrder get(UserDetails owner, UUID uuid) {
        return repository.findByOwnerAndUuid(owner, uuid).orElseThrow();
    }

    public long countMy(UserDetails owner) {
        return repository.countAllByOwner(owner);
    }

    @Override
    public JpaRepository<BuyingOrder, UUID> getRepository() {
        return repository;
    }
}
