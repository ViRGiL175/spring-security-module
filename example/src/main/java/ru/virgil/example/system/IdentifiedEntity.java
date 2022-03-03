package ru.virgil.example.system;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class IdentifiedEntity extends BaseEntity {

    @GeneratedValue
    @Id
    private UUID uuid;

}
