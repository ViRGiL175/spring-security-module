package ru.virgil.utils.image;

import java.nio.file.Path;

public interface IPrivateImage<Owner extends IBaseEntity> extends IBaseEntity {

    Path getFileLocation();

    void setFileLocation(Path location);

    Owner getOwner();

    void setOwner(Owner owner);
}
