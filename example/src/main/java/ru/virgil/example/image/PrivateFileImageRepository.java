package ru.virgil.example.image;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.virgil.example.user.UserDetails;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrivateFileImageRepository extends CrudRepository<PrivateFileImage, UUID> {

    Optional<PrivateFileImage> findByOwnerAndUuid(UserDetails owner, UUID imageUuid);

}
