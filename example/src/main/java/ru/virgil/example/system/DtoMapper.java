package ru.virgil.example.system;

public interface DtoMapper<D, E> {

    D toDto(E entity);

}
