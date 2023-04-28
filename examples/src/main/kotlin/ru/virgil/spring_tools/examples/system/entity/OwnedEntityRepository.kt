package ru.virgil.spring_tools.examples.system.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import ru.virgil.spring_tools.examples.user.UserDetails
import java.util.*

@NoRepositoryBean
interface OwnedEntityRepository<Entity : OwnedEntity> : JpaRepository<Entity, UUID> {

    fun findAllByOwner(owner: UserDetails): List<Entity>
    fun findAllByOwnerAndDeletedFalse(owner: UserDetails): List<Entity>
    fun findAllByOwnerIsNull(): MutableList<Entity>

}
