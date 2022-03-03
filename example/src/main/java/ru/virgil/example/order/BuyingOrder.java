package ru.virgil.example.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.virgil.example.system.IdentifiedEntity;
import ru.virgil.example.truck.Truck;
import ru.virgil.example.user.UserDetails;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class BuyingOrder extends IdentifiedEntity {

    @ManyToOne
    private UserDetails owner;
    @OneToMany
    private List<Truck> trucks = new ArrayList<>();
    private String description;

}
