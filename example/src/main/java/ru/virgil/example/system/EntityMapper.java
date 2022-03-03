package ru.virgil.example.system;

public interface EntityMapper<D, E> {

    E toEntity(D dto);

}
