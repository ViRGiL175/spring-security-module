package ru.virgil.example.system;

import lombok.Data;
import lombok.experimental.ExtensionMethod;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@ExtensionMethod(value = {LocalDateTime.class, DateApiExtensions.class}, suppressBaseMethods = false)
public abstract class BaseEntity {

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public LocalDateTime getCreatedAt() {
        return createdAt.databaseTruncate();
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt.databaseTruncate();
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt.databaseTruncate();
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt.databaseTruncate();
    }
}
