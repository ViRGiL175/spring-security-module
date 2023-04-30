package ru.virgil.spring_tools.examples.security

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import ru.virgil.spring_tools.examples.system.entity.BaseEntity
import ru.virgil.spring_tools.tools.security.entity.SecurityUser
import java.io.Serializable
import java.util.*
import java.util.stream.Collectors

/**
 * Не наследуется от [IdentifiedEntity] из-за бага Detached Entity.
 */
@Entity
class SecurityUserDetails(
    @Id
    @GeneratedValue
    var uuid: UUID = UUID.randomUUID(),
    @ElementCollection(fetch = FetchType.EAGER)
    var userAuthorities: MutableSet<UserAuthority>,
    override var banned: Boolean = false,
    override var firebaseUserId: String,
) : BaseEntity(), SecurityUser, Serializable {

    override val authorities: Collection<GrantedAuthority>
        get() = userAuthorities.stream()
            .map { userAuthority: UserAuthority -> SimpleGrantedAuthority(userAuthority.name) }
            .collect(Collectors.toSet())

}
