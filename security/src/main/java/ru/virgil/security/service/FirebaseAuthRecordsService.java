package ru.virgil.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.virgil.security.entity.AuthRecord;
import ru.virgil.security.header.AuthorizationHeader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.google.common.truth.Truth.assertThat;
import static ru.virgil.security.entity.AuthMethods.FIREBASE;

@RequiredArgsConstructor
@Service
public class FirebaseAuthRecordsService {

    public static final String AUTH_RECORD_UUID_CREDENTIAL = "auth_record_uuid";
    private final Map<UUID, AuthRecord> authRecordMap = new HashMap<>();
    private final ObjectMapper jackson;

    public UUID createAuthRecord() {
        UUID uuid = UUID.randomUUID();
        authRecordMap.put(uuid, new AuthRecord(null, null));
        return uuid;
    }

    public AuthRecord getAuthRecord(UUID uuid) {
        return authRecordMap.get(uuid);
    }

    public void editAuthRecord(UUID uuid, AuthRecord authRecord) {
        authRecordMap.put(uuid, authRecord);
    }

    public void deleteAuthRecord(UUID uuid) {
        authRecordMap.remove(uuid);
    }

    public UUID extractAuthRecordUuid(String rawAuthorizationHeader) {
        try {
            AuthorizationHeader authorizationHeader = jackson.readValue(rawAuthorizationHeader,
                    AuthorizationHeader.class);
            assertThat(authorizationHeader.authScheme()).isEqualTo(FIREBASE);
            assertThat(authorizationHeader.credentials()).containsKey(AUTH_RECORD_UUID_CREDENTIAL);
            return UUID.fromString(authorizationHeader.credentials().get(AUTH_RECORD_UUID_CREDENTIAL));
        } catch (AssertionError | IllegalArgumentException | IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        }
    }
}
