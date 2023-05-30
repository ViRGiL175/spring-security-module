package ru.virgil.spring_tools.tools.security.oauth

interface SecurityUser : UserDetailsKt {

    val firebaseUserId: String
}
