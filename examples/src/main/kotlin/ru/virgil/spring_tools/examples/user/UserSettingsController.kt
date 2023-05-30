package ru.virgil.spring_tools.examples.user

import org.springframework.web.bind.annotation.*

@CrossOrigin(
    origins = ["http://localhost:4200/"],
    allowCredentials = true.toString()
)
@RestController
@RequestMapping("/user_settings")
class UserSettingsController(
    private val userSettingsService: UserSettingsService,
) : UserSettingsMapper {

    @GetMapping
    fun get(): UserSettingsDto {
        val currentUser = userSettingsService.get()
        return currentUser.toDto()
    }

    // TODO: Покрыть тестом
    @PutMapping
    fun put(@RequestBody userSettingsDto: UserSettingsDto): UserSettingsDto {
        val editedUserSettings = userSettingsService.edit(userSettingsDto.toEntity())
        return editedUserSettings.toDto()
    }
}
