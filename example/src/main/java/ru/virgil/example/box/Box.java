package ru.virgil.example.box;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.virgil.example.system.IdentifiedEntity;
import ru.virgil.example.truck.Truck;
import ru.virgil.example.user.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class Box extends IdentifiedEntity {

    @ManyToOne
    private UserDetails owner;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Truck truck;
    private String description;
    private int price;
    private float weight;

}
