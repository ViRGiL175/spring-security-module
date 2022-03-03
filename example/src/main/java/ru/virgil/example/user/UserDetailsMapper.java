package ru.virgil.example.user;

import org.mapstruct.Mapper;
import ru.virgil.example.system.BaseMapper;

@Mapper
public interface UserDetailsMapper extends BaseMapper<UserDetailsDto, UserDetails> {

}

