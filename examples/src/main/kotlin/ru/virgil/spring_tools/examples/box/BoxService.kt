package ru.virgil.spring_tools.examples.box

import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.virgil.spring_tools.examples.truck.Truck
import ru.virgil.spring_tools.examples.user.UserDetails
import ru.virgil.spring_tools.examples.user.UserDetailsService
import java.util.*


@Service
class BoxService(
    @Qualifier(UserDetailsService.current)
    private val ownerProvider: ObjectProvider<UserDetails>,
    private val boxRepository: BoxRepository,
) : BoxMapper {

    private val owner by lazy { ownerProvider.getObject() }

    fun getAll(page: Int, size: Int): List<Box> =
        boxRepository.findAllByOwnerAndDeletedIsFalse(owner, PageRequest.of(page, size))

    fun getAll(truck: Truck, page: Int, size: Int): List<Box> =
        boxRepository.findAllByOwnerAndTruckAndDeletedIsFalse(owner, truck, PageRequest.of(page, size))

    fun get(uuid: UUID): Box = boxRepository.findByOwnerAndUuidAndDeletedIsFalse(owner, uuid).orElseThrow()

    fun create(truck: Truck, boxDto: BoxDto): Box {
        val box = boxDto.toEntity(owner, truck)
        return boxRepository.save(box)
    }

    fun edit(uuid: UUID, patchBox: BoxDto): Box {
        var box = get(uuid)
        box = box.merge(patchBox)
        return boxRepository.save(box)
    }

    fun delete(uuid: UUID) {
        val box = get(uuid)
        box.deleted = true
        boxRepository.save(box)
    }

    fun getAllMyWeapons(): List<Box> = boxRepository.findAllByOwnerAndTypeAndDeletedIsFalse(owner, BoxType.WEAPON)

    fun countMy(): Long = boxRepository.countAllByOwnerAndDeletedIsFalse(owner)
}
