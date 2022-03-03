package ru.virgil.example.user;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.virgil.example.system.EntityMerger;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserDetailsMerger extends EntityMerger<UserDetails> {

}
