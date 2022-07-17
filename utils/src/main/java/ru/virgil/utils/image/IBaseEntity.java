package ru.virgil.utils.image;

import java.time.LocalDateTime;
import java.util.UUID;

public interface IBaseEntity {

    UUID getUuid();

    void setUuid(UUID uuid);

    LocalDateTime getCreatedAt();

    void setCreatedAt(LocalDateTime createdAt);

    LocalDateTime getUpdatedAt();

    void setUpdatedAt(LocalDateTime updatedAt);

}
