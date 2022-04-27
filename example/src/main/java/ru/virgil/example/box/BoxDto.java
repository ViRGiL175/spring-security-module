package ru.virgil.example.box;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.virgil.example.system.IdentifiedDto;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class BoxDto extends IdentifiedDto {

    private BoxType type;
    private String description;
    private int price;
    private float weight;

}
