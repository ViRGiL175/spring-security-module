package ru.virgil.example.truck;

import org.mapstruct.Mapper;
import ru.virgil.example.system.BaseMapper;

@Mapper
public interface TruckMapper extends BaseMapper<TruckDto, Truck> {

}
