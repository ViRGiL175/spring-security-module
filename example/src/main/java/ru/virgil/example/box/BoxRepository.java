package ru.virgil.example.box;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.virgil.example.truck.Truck;
import ru.virgil.example.user.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BoxRepository extends JpaRepository<Box, UUID> {

    Optional<Box> findByOwnerAndUuid(UserDetails owner, UUID uuid);

    List<Box> findAllByOwner(UserDetails owner, Pageable pageable);

    List<Box> findAllByOwner(UserDetails owner);

    List<Box> findAllByOwnerAndTruck(UserDetails owner, Truck truck, Pageable pageable);

    long countAllByOwner(UserDetails owner);

}
