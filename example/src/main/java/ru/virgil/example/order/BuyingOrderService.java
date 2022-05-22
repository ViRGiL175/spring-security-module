package ru.virgil.example.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.virgil.example.user.UserDetails;
import ru.virgil.example.user.UserDetailsService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuyingOrderService {

    @Getter
    private final BuyingOrderRepository repository;
    private final UserDetailsService userDetailsService;

    public List<BuyingOrder> getAll(int page, int size) {
        return repository.findAllByOwner(userDetailsService.getCurrentUser(), PageRequest.of(page, size));
    }

    public BuyingOrder get(UUID uuid) {
        return repository.findByOwnerAndUuid(userDetailsService.getCurrentUser(), uuid).orElseThrow();
    }

    public long countMy(UserDetails owner) {
        return repository.countAllByOwner(userDetailsService.getCurrentUser());
    }
}
