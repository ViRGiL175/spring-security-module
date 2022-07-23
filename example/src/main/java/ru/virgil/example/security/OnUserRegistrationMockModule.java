package ru.virgil.example.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.virgil.example.box.Box;
import ru.virgil.example.box.BoxMockService;
import ru.virgil.example.order.BuyingOrder;
import ru.virgil.example.order.BuyingOrderMockService;
import ru.virgil.example.system.Module;
import ru.virgil.example.truck.Truck;
import ru.virgil.example.truck.TruckMockService;
import ru.virgil.example.user.UserDetails;
import ru.virgil.utils.FakerUtils;

import javax.persistence.Entity;
import java.util.List;

/**
 * {@link Controller} – HTTP-слой, API, верхний слой простых операций над данными.
 * <p>
 * Например, получаем юзера от этого сервиса, с его помощью получаем коробку вот из этого сервиса и отдаем как DTO.
 * <p>
 * Может агрегировать: {@link Service}, {@link Component}, {@link Module}
 * <p>
 * {@link Service} – простой бизнес-слой. Сервисы представляют доступ к данным своих доменов и производят простые и
 * CRUD операции над ними. Другие данные получают только извне в виде параметров.
 * <p>
 * Например, меняем параметры коробки, создаем новую коробку, удаляем коробки. Сервис оперирует {@link Entity}
 * и редактирует БД через {@link Repository}.
 * <p>
 * Может агрегировать: {@link Component}, {@link Module}
 * <p>
 * {@link Component} – утилита, библиотека и т.д. Внешний функциональный модуль, не имеющий своего домена, своих данных.
 * <p>
 * Например, дополняем DataFaker новыми данными, делаем обертку над библиотекой Firebase, чтобы было удобно
 * использовать в разных частях системы.
 * <p>
 * Может агрегировать: {@link Component}, {@link Module}
 * <p>
 * {@link Module} – сложный бизнес-слой. Модули производят сложные операции, которые требуют асинхронности,
 * доступа к нескольким доменам сразу, имеют запутанную логику.
 * <p>
 * Например, водитель грузовика вышел на линию, он ждет асинхронно входящих к нему заказов,
 * высчитывается самый эффективный водитель для конкретного заказа, водители реагируют на заказы.
 * <p>
 * Может агрегировать: {@link Service}, {@link Component}, {@link Module}
 */
@Component
@RequiredArgsConstructor
public class OnUserRegistrationMockModule {

    private final BuyingOrderMockService buyingOrderMockService;
    private final TruckMockService truckMockService;
    private final BoxMockService boxMockService;
    private final FakerUtils faker;

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
