package ru.virgil.spring_tools.examples.order

interface BuyingOrderMapper {

    fun BuyingOrder.toDto(): BuyingOrderDto = BuyingOrderDto(
        createdAt,
        updatedAt,
        uuid,
        description,
    )

    infix fun BuyingOrder.merge(buyingOrderDto: BuyingOrderDto): BuyingOrder {
        description = buyingOrderDto.description ?: description
        return this
    }
}
