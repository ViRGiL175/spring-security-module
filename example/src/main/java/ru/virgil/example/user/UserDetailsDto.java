package ru.virgil.example.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.virgil.example.system.IdentifiedDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDetailsDto extends IdentifiedDto {

    private String name;

}
