package ru.virgil.example.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.virgil.example.system.IdentifiedEntity;
import ru.virgil.security.entity.SecurityUser;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class SecurityUserDetails extends IdentifiedEntity implements SecurityUser {

    @ElementCollection
    private Set<UserAuthority> authorities = new HashSet<>();
    private boolean banned;
    private String firebaseUserId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream()
                .map(userAuthority -> new SimpleGrantedAuthority(userAuthority.name()))
                .collect(Collectors.toSet());
    }

}
