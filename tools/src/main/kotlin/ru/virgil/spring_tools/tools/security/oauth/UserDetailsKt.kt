package ru.virgil.spring_tools.tools.security.oauth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

// TODO: Наследование без создания spring-полей?
/**
 * Интерфейс создан для удобства и для исправления коллизий с UserDetails
 * */
interface UserDetailsKt : UserDetails {

    val springAuthorities: MutableCollection<String>
    val springUsername: String
    val springPassword: String
    val springAccountNonExpired: Boolean
    val springAccountNonLocked: Boolean
    val springCredentialsNonExpired: Boolean
    val springEnabled: Boolean

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        springAuthorities.map { SimpleGrantedAuthority(it) }.toMutableSet()

    override fun getPassword(): String = springPassword

    override fun getUsername(): String = springUsername

    override fun isAccountNonExpired(): Boolean = springAccountNonExpired

    override fun isAccountNonLocked(): Boolean = springAccountNonLocked

    override fun isCredentialsNonExpired(): Boolean = springCredentialsNonExpired

    override fun isEnabled(): Boolean = springEnabled
}
