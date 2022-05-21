package ru.virgil.example.box;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoxSecurity {

    public boolean hasWeapon(BoxDto boxDto) {
        return boxDto.getType() == BoxType.WEAPON;
    }

}
