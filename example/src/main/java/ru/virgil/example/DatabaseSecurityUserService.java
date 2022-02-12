package ru.virgil.example;

import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.virgil.security.entity.SecurityUser;
import ru.virgil.security.service.SecurityUserService;

@Primary
@Service
public class DatabaseSecurityUserService implements SecurityUserService {

    @Override
    public SecurityUser registerOrLogin(Authentication authentication) {
        return null;
    }
}
