package ru.virgil.example.truck;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.virgil.example.system.IdentifiedDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class TruckDto extends IdentifiedDto {

    private int boxesCount;

}
