package ru.virgil.example.truck;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.virgil.example.box.Box;
import ru.virgil.example.order.BuyingOrder;
import ru.virgil.example.system.IdentifiedEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class Truck extends IdentifiedEntity {

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Box> boxes = new ArrayList<>();
    @ManyToOne
    private BuyingOrder buyingOrder;

}
