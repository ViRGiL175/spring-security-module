package ru.virgil.example.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.virgil.example.security.SecurityUserDetails;
import ru.virgil.example.system.IdentifiedEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class UserDetails extends IdentifiedEntity {

    @OneToOne
    private SecurityUserDetails securityUserDetails;
    private String name;

}
