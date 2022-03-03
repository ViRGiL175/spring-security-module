package ru.virgil.example.order;

import org.mapstruct.Mapper;
import ru.virgil.example.system.BaseMapper;

@Mapper
public interface BuyingOrderMapper extends BaseMapper<BuyingOrderDto, BuyingOrder> {

}
