package ru.virgil.spring_tools.examples.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.virgil.spring_tools.examples.security.SecurityUserDetails
import java.util.*

@Repository
interface UserDetailsRepository : JpaRepository<UserDetails, UUID> {

    fun findBySecurityUserDetails(securityUserDetails: SecurityUserDetails): Optional<UserDetails>

}
