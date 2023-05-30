package ru.virgil.spring_tools.examples.security

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SecurityUserRepository : CrudRepository<SecurityUser, UUID> {

    fun findByFirebaseUserId(firebaseUserId: String): SecurityUser?

    fun findBySpringUsername(username: String): SecurityUser?
}
