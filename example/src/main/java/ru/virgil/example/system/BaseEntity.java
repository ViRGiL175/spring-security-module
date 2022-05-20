package ru.virgil.example.system;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.annotation.Nullable;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@MappedSuperclass
@Data
public abstract class BaseEntity {

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public LocalDateTime getCreatedAt() {
        return truncate(createdAt);
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = truncate(createdAt);
    }

    public LocalDateTime getUpdatedAt() {
        return truncate(updatedAt);
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = truncate(updatedAt);
    }

    @Nullable
    private LocalDateTime truncate(@Nullable LocalDateTime original) {
        return original == null ? null : original.truncatedTo(ChronoUnit.MILLIS);
    }
}
