package ru.virgil.example.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SecurityUserDetailsRepository extends JpaRepository<SecurityUserDetails, UUID> {

    Optional<SecurityUserDetails> findByFirebaseUserId(String firebaseUserId);

}
