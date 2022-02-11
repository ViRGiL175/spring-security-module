package ru.virgil.security.service;

import org.springframework.security.core.Authentication;
import ru.virgil.security.entity.SecurityUser;

public interface SecurityUserService {

    SecurityUser registerOrLogin(Authentication authentication);
}
