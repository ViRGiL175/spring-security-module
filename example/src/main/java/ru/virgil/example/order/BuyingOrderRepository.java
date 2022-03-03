package ru.virgil.example.order;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.virgil.example.user.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BuyingOrderRepository extends JpaRepository<BuyingOrder, UUID> {

    List<BuyingOrder> findAllByOwner(UserDetails owner, Pageable pageable);

    Optional<BuyingOrder> findByOwnerAndUuid(UserDetails owner, UUID uuid);

    long countAllByOwner(UserDetails owner);

}
