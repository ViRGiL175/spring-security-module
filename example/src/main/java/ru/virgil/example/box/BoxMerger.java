package ru.virgil.example.box;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.virgil.example.system.EntityMerger;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BoxMerger extends EntityMerger<Box> {

}
