package ru.virgil.security.header;

import ru.virgil.security.entity.AuthMethods;

import java.util.Map;

public record WwwAuthenticateHeader(AuthMethods authScheme, Map<String, String> challenges) {

}
