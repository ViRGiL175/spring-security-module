package ru.virgil.example.box;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.virgil.example.system.IdentifiedDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class BoxDto extends IdentifiedDto {

    private BoxType type;
    private String description;
    private int price;
    private float weight;

}
