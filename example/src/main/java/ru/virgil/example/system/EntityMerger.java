package ru.virgil.example.system;

import org.mapstruct.MappingTarget;

public interface EntityMerger<E> {

    E merge(@MappingTarget E source, E patch);

}
