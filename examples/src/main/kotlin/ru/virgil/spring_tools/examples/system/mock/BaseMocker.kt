package ru.virgil.spring_tools.examples.system.mock

import org.springframework.data.jpa.repository.JpaRepository
import ru.virgil.spring_tools.examples.system.entity.IdentifiedEntity
import ru.virgil.spring_tools.examples.system.entity.OwnedEntity
import ru.virgil.spring_tools.examples.system.entity.OwnedEntityRepository
import ru.virgil.spring_tools.examples.user.UserDetails
import java.util.*

abstract class BaseMocker {

    /**
     * При мокировании сначала может понадобиться найти какой-то определенный элемент. Например, грузовик без ящиков,
     * чтобы заполнить его ящиками. А если грузовиков без ящиков не осталось, то можно взять случайный грузовик.
     */
    protected fun <Entity : OwnedEntity> OwnedEntityRepository<Entity>.findPriorityOrGetRandom(
        owner: UserDetails,
        priorityPredicate: (Entity) -> Boolean,
    ): Entity {
        val entities = this.findAllByOwner(owner)
        val priorityEntities = entities.filter(priorityPredicate)
        return if (priorityEntities.isNotEmpty()) {
            priorityEntities.random()
        } else {
            entities.random()
        }
    }

    /**
     * При мокировании сначала может понадобиться найти какой-то определенный элемент. Например, грузовик без ящиков,
     * чтобы заполнить его ящиками. А если грузовиков без ящиков не осталось, то можно взять случайный грузовик.
     *
     */
    fun <Entity : IdentifiedEntity> JpaRepository<Entity, UUID>.findPriorityOrGetRandom(
        priorityPredicate: (Entity) -> Boolean,
    ): Entity {
        val entities = this.findAll()
        val priorityEntities = entities.filter(priorityPredicate)
        return if (priorityEntities.isNotEmpty()) {
            priorityEntities.random()
        } else {
            entities.random()
        }
    }

}
