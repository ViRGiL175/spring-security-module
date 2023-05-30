package ru.virgil.spring_tools.examples.security

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import ru.virgil.spring_tools.tools.security.oauth.SecurityUser
import ru.virgil.spring_tools.tools.util.data.Identified
import ru.virgil.spring_tools.tools.util.data.Timed
import java.time.LocalDateTime
import java.util.*

@Entity
class SecurityUser(
    override val firebaseUserId: String,
    userAuthorities: MutableCollection<SecurityUserAuthority>,
    override val springUsername: String = UUID.randomUUID().toString(),
    override val springPassword: String = UUID.randomUUID().toString(),
    // TODO: Настроить базовый Entity на использование стандартных интерфейсов
) : SecurityUser, Identified, Timed {

    @Id
    @GeneratedValue
    override lateinit var uuid: UUID

    @CreationTimestamp
    override lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    override lateinit var updatedAt: LocalDateTime

    @ElementCollection(fetch = FetchType.EAGER)
    override val springAuthorities: MutableCollection<String> = userAuthorities.map { it.toString() }.toMutableSet()

    override val springAccountNonExpired: Boolean = true
    override val springAccountNonLocked: Boolean = true
    override val springCredentialsNonExpired: Boolean = true
    override val springEnabled: Boolean = true
}
