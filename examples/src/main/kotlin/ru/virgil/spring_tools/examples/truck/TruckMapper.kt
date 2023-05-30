package ru.virgil.spring_tools.examples.truck

interface TruckMapper {

    fun Truck.toDto() = TruckDto(createdAt, updatedAt, uuid, boxes.size)
}
