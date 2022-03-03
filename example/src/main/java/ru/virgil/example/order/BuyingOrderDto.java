package ru.virgil.example.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.virgil.example.system.IdentifiedDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class BuyingOrderDto extends IdentifiedDto {

    private String description;

}
