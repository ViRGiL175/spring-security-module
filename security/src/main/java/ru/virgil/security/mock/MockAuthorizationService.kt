package ru.virgil.security.mock

import ru.virgil.security.entity.SecurityUser
import ru.virgil.security.firebase.FirebaseAuthenticationToken

interface MockAuthorizationService {

    fun authOrRegister(token: FirebaseAuthenticationToken): SecurityUser
}
