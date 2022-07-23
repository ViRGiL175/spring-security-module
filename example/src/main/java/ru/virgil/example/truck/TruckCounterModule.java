package ru.virgil.example.truck;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.virgil.example.order.BuyingOrder;
import ru.virgil.example.order.BuyingOrderService;
import ru.virgil.example.user.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Это пример функционального модуля.
 * <p>
 * Функциональные модули проводят операции над данными, которые шире и сложнее, чем CRUD.
 * Например, асинхронные операции к нескольким доменам сразу. Типа, водитель грузовика делает запрос в систему
 * и смотрит на входящие заказы.
 * <p>
 * Функциональные модули имеют доступ к нескольким доменам сразу через агрегацию их сервисов.
 * Но сервисы не должны вкладываться один в другой, чтобы избежать зацикливания.
 * Только функциональные модули могут нарушать эту инкапсуляцию.
 * <p>
 * CRUD и простые операции над данными должны проводиться только через соответствующие методы сервисов доменов.
 * Чтобы не изобретать велосипед и соблюдать DRY.
 */
@Component
@RequiredArgsConstructor
public class TruckCounterModule {

    private final TruckService truckService;
    private final BuyingOrderService buyingOrderService;

    public long countAllTrucks(UserDetails owner) {
        return getAllTrucks(owner).size();
    }

    public Set<Truck> getAllTrucks(UserDetails owner) {
        // todo: почему-то тесты проходят только с полностью дропнутой базы
        List<BuyingOrder> buyingOrders = buyingOrderService.getAll(owner, 0, Integer.MAX_VALUE);
        return buyingOrders.stream()
                .map(buyingOrder -> truckService.getAll(buyingOrder, 0, Integer.MAX_VALUE))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

}
