package ru.virgil.example.util.security

import org.springframework.stereotype.Service
import ru.virgil.example.security.SecurityUserDetails
import ru.virgil.example.security.SecurityUserDetailsService
import ru.virgil.security.firebase.FirebaseAuthenticationToken
import ru.virgil.security.mock.MockAuthorizationService

@Service
class MockAuthorizationService(
    private val securityUserDetailsService: SecurityUserDetailsService,
) : MockAuthorizationService {

    override fun authOrRegister(token: FirebaseAuthenticationToken): SecurityUserDetails {
        return securityUserDetailsService.repository.findByFirebaseUserId(token.principal.toString())
            .orElseGet { securityUserDetailsService.register(token) }
    }
}
