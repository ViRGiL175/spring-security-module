package ru.virgil.spring_tools.tools.security.service

import org.springframework.security.core.Authentication
import ru.virgil.spring_tools.tools.security.entity.SecurityUser

interface SecurityUserService {

    fun registerOrLogin(authentication: Authentication): SecurityUser

}
