package ru.virgil.example.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.virgil.utils.image.IBaseEntity;
import ru.virgil.example.security.SecurityUserDetails;
import ru.virgil.example.system.IdentifiedEntity;

import javax.persistence.OneToOne;

@javax.persistence.Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class UserDetails extends IdentifiedEntity implements IBaseEntity {

    @OneToOne
    private SecurityUserDetails securityUserDetails;
    private String name;

}
