package ru.virgil.spring_tools.examples.user

interface UserDetailsMapper {

    fun UserDetails.toDto(): UserDetailsDto = UserDetailsDto(createdAt, updatedAt, uuid, name)

}
