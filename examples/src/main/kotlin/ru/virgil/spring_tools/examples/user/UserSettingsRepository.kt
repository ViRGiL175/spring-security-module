package ru.virgil.spring_tools.examples.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository
import ru.virgil.spring_tools.examples.system.entity.OwnedRepository
import java.util.*

@Repository
interface UserSettingsRepository : JpaRepository<UserSettings, UUID>, OwnedRepository<UserSettings> {

    fun findByCreatedBy(createdBy: UserDetails): Optional<UserSettings>
}
