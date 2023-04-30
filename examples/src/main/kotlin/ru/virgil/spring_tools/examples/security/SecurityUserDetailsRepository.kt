package ru.virgil.spring_tools.examples.security

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SecurityUserDetailsRepository : JpaRepository<SecurityUserDetails, UUID> {

    fun findByFirebaseUserId(firebaseUserId: String): Optional<SecurityUserDetails>

}
