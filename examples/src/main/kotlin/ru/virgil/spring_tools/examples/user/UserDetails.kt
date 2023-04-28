package ru.virgil.spring_tools.examples.user

import jakarta.persistence.Entity
import jakarta.persistence.OneToOne

import ru.virgil.spring_tools.examples.security.SecurityUserDetails
import ru.virgil.spring_tools.examples.system.entity.IdentifiedEntity
import ru.virgil.spring_tools.tools.util.base.entity.Identified
import ru.virgil.spring_tools.tools.util.base.entity.Timed

@Entity
class UserDetails(
    @OneToOne
    private val securityUserDetails: SecurityUserDetails,
    var name: String? = null,
) : IdentifiedEntity(), Identified, Timed
