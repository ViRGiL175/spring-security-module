package ru.virgil.example.util.security

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import ru.virgil.example.security.SecurityUserDetailsService
import ru.virgil.example.security.UserAuthority
import ru.virgil.security.InternalSecurityToken
import ru.virgil.security.firebase.FirebaseAuthenticationToken

abstract class MockSecurityContextFactory(
    private val securityUserDetailsService: SecurityUserDetailsService,
) {

    fun createSecurityContext(userId: String, authToken: String, vararg authorities: UserAuthority): SecurityContext {
        val securityContext = SecurityContextHolder.createEmptyContext()
        val firebaseAuthenticationToken = FirebaseAuthenticationToken(
            authorities.map { SimpleGrantedAuthority(it.name) }.toMutableSet(),
            userId,
            authToken
        )
        val securityUser = securityUserDetailsService.registerOrLogin(firebaseAuthenticationToken)
        val internalSecurityToken = InternalSecurityToken(firebaseAuthenticationToken.authorities, securityUser)
        securityContext.authentication = internalSecurityToken
        return securityContext
    }
}
