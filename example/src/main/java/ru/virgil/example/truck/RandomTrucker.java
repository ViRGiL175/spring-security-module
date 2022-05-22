package ru.virgil.example.truck;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.virgil.example.truck.Truck;
import ru.virgil.example.truck.TruckService;

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
public class RandomTrucker {

    private final TruckService truckService;

    public Truck getRandomTruck() {
        return truckService.getRandom();
    }
}
