package ru.virgil.spring_tools.examples.box

import ru.virgil.spring_tools.examples.truck.Truck

interface BoxMapper {

    fun BoxDto.toEntity(truck: Truck): Box =
        Box(type!!, truck, description!!, price!!, weight!!)

    fun Box.toDto(): BoxDto =
        BoxDto(uuid, createdAt, updatedAt, type, description, price, weight)

    infix fun Box.merge(boxDto: BoxDto): Box {
        price = boxDto.price ?: price
        type = boxDto.type ?: type
        description = boxDto.description ?: description
        weight = boxDto.weight ?: weight
        return this
    }
}
