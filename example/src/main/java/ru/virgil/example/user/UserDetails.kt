package ru.virgil.example.user

import ru.virgil.example.security.SecurityUserDetails
import ru.virgil.example.system.entity.IdentifiedEntity
import ru.virgil.utils.image.IBaseEntity
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
class UserDetails(
    @OneToOne
    val securityUserDetails: SecurityUserDetails,
    var name: String? = null,
) : IdentifiedEntity(), IBaseEntity
