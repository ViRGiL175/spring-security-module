package ru.virgil.example.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.virgil.example.security.SecurityUserDetails;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, UUID> {

    Optional<UserDetails> findBySecurityUserDetails(SecurityUserDetails securityUserDetails);

}
