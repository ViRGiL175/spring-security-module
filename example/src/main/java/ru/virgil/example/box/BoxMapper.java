package ru.virgil.example.box;

import org.mapstruct.Mapper;
import ru.virgil.example.system.BaseMapper;

@Mapper
public interface BoxMapper extends BaseMapper<BoxDto, Box> {

}
