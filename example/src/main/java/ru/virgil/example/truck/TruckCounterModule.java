package ru.virgil.example.truck;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.virgil.example.order.BuyingOrder;
import ru.virgil.example.order.BuyingOrderService;
import ru.virgil.example.user.UserDetails;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Это пример функционального компонента.
 * <p>
 * Функциональные компоненты проводят операции над данными, которые шире и сложнее, чем CRUD.
 * Например, асинхронные операции к нескольким доменам сразу. Типа, водитель грузовика делает запрос в систему
 * и смотрит на входящие заказы.
 * <p>
 * Функциональные компоненты имеют доступ к нескольким доменам сразу через агрегацию их сервисов.
 * Сервисы не должны вкладываться один в другой, чтобы избежать зацикливания.
 * Только функциональные компоненты могут нарушать эту инкапсуляцию.
 * <p>
 * CRUD и простые операции над данными должны проводиться только через соответствующие методы сервисов доменов.
 * Чтобы не изобретать велосипед.
 */
@Component
@RequiredArgsConstructor
public class TruckCounterModule {

    private final TruckService truckService;
    private final BuyingOrderService buyingOrderService;

    public long countAll(UserDetails owner) {
        // todo: почему-то тесты проходят только с полностью дропнутой базы
        List<BuyingOrder> buyingOrders = buyingOrderService.getAll(owner, 0, Integer.MAX_VALUE);
        Set<Truck> trucksSet = new HashSet<>();
        buyingOrders.forEach(buyingOrder -> {
            List<Truck> trucks = truckService.getAll(buyingOrder, 0, Integer.MAX_VALUE);
            trucksSet.addAll(trucks);
        });
        return trucksSet.size();
    }

}
