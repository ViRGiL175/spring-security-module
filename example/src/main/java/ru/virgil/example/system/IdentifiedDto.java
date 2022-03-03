package ru.virgil.example.system;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class IdentifiedDto extends BaseDto {

    private UUID uuid;

}
