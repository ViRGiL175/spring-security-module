package ru.virgil.utils.image;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface IPrivateImageRepository<Owner extends IBaseEntity, PrivateImage extends IPrivateImage<Owner>>
        extends CrudRepository<PrivateImage, UUID> {

    Optional<PrivateImage> findByOwnerAndUuid(Owner owner, UUID imageUuid);

}
