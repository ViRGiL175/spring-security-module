package ru.virgil.spring_tools.examples.user

interface UserSettingsMapper {

    fun UserSettingsDto.toEntity(): UserSettings = UserSettings(name)

    fun UserSettings.toDto(): UserSettingsDto = UserSettingsDto(createdAt, updatedAt, uuid, name)

    infix fun UserSettings.merge(userSettings: UserSettings): UserSettings {
        name = userSettings.name ?: name
        return this
    }
}
