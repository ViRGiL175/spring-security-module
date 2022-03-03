package ru.virgil.example.security;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import ru.virgil.example.box.Box;
import ru.virgil.example.box.BoxMockService;
import ru.virgil.example.order.BuyingOrder;
import ru.virgil.example.order.BuyingOrderMockService;
import ru.virgil.example.truck.Truck;
import ru.virgil.example.truck.TruckMockService;
import ru.virgil.example.user.UserDetails;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OnUserRegistrationMockService {

    private final BuyingOrderMockService buyingOrderMockService;
    private final TruckMockService truckMockService;
    private final BoxMockService boxMockService;
    private final Faker faker;

    public UserDetails mock(UserDetails userDetails) {
        userDetails.setName(faker.name().fullName());
        List<BuyingOrder> buyingOrders = buyingOrderMockService.mock(userDetails, 15);
        List<Box> boxes = boxMockService.mock(userDetails, 50);
        List<Truck> trucks = truckMockService.mock(userDetails, 5);
        boxes = boxMockService.bind(boxes, trucks);
        trucks = truckMockService.bind(trucks, buyingOrders);
        return userDetails;
    }

}
