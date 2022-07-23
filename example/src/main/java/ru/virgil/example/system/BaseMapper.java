package ru.virgil.example.system;

public interface BaseMapper<D extends BaseDto, E extends BaseEntity> {

    D toDto(E entity);

    E toEntity(D dto);
}
