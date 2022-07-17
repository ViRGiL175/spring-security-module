package ru.virgil.example.image;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.virgil.example.system.IdentifiedDto;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PrivateImageFileDto extends IdentifiedDto {

    private String name;

}
