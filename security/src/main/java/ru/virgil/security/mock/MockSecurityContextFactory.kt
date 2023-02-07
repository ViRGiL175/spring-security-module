package ru.virgil.security.mock

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory
import ru.virgil.security.InternalSecurityToken
import ru.virgil.security.firebase.FirebaseAuthenticationToken
import java.util.*


abstract class MockSecurityContextFactory<A : Annotation>(
    private val mockAuthorizationService: MockAuthorizationService,
) : WithSecurityContextFactory<A> {

    /**
     * @param userId      if this value is empty string then it will be replaced by random value
     * @param authToken   if this value is empty string then it will be replaced by random value
     * @param authorities collection of required authorities
     */
    fun authenticate(userId: String, authToken: String, authorities: Collection<GrantedAuthority>): SecurityContext {
        var userId = userId
        var authToken = authToken
        val securityContext = SecurityContextHolder.createEmptyContext()
        userId = if (userId.isEmpty()) UUID.randomUUID().toString() else authToken
        authToken = authToken.ifEmpty { UUID.randomUUID().toString() }
        val token = FirebaseAuthenticationToken(authorities, userId, authToken)
        val userDetails = mockAuthorizationService.authOrRegister(token)
        val internalSecurityToken = InternalSecurityToken(token.authorities, userDetails)
        securityContext.authentication = internalSecurityToken
        return securityContext
    }
}
