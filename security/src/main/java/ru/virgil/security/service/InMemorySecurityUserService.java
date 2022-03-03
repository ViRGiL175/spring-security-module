package ru.virgil.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.virgil.security.entity.BaseSecurityUser;
import ru.virgil.security.entity.SecurityUser;

import java.util.HashSet;
import java.util.Set;

@Service
public class InMemorySecurityUserService implements SecurityUserService {

    private final Set<SecurityUser> baseSecurityUsers = new HashSet<>();

    @Override
    public SecurityUser registerOrLogin(Authentication authentication) {
        return baseSecurityUsers.stream()
                .filter(securityUser -> securityUser.getFirebaseUserId()
                        .equals(authentication.getPrincipal().toString()))
                .findAny()
                .orElseGet(() -> registerNewUser(authentication));
    }

    private SecurityUser registerNewUser(Authentication authentication) {
        BaseSecurityUser securityUserDetails = new BaseSecurityUser(authentication.getAuthorities());
        securityUserDetails.setFirebaseUserId(authentication.toString());
        baseSecurityUsers.add(securityUserDetails);
        return securityUserDetails;
    }
}
