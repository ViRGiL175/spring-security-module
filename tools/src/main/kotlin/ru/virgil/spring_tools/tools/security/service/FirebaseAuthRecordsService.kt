package ru.virgil.spring_tools.tools.security.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.truth.Truth
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import ru.virgil.spring_tools.tools.security.entity.AuthMethods
import ru.virgil.spring_tools.tools.security.entity.AuthRecord
import ru.virgil.spring_tools.tools.security.header.AuthorizationHeader
import java.io.IOException
import java.util.*

@Service
class FirebaseAuthRecordsService(
    private val jackson: ObjectMapper,
) {

    private val authRecords: MutableMap<UUID, AuthRecord> = HashMap()

    fun createAuthRecord(): UUID {
        val uuid = UUID.randomUUID()
        authRecords[uuid] = AuthRecord(null, null)
        return uuid
    }

    fun getAuthRecord(uuid: UUID): AuthRecord? = authRecords[uuid]

    fun editAuthRecord(uuid: UUID, authRecord: AuthRecord) {
        authRecords[uuid] = authRecord
    }

    fun deleteAuthRecord(uuid: UUID) {
        authRecords.remove(uuid)
    }

    fun extractAuthRecordUuid(rawAuthorizationHeader: String): UUID = try {
        val authorizationHeader = jackson.readValue(
            rawAuthorizationHeader,
            AuthorizationHeader::class.java
        )
        Truth.assertThat(authorizationHeader.authScheme).isEqualTo(AuthMethods.FIREBASE)
        Truth.assertThat(authorizationHeader.credentials).containsKey(AUTH_RECORD_UUID_CREDENTIAL)
        UUID.fromString(authorizationHeader.credentials[AUTH_RECORD_UUID_CREDENTIAL])
    } catch (e: AssertionError) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.localizedMessage)
    } catch (e: IllegalArgumentException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.localizedMessage)
    } catch (e: IOException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.localizedMessage)
    }

    companion object {

        const val AUTH_RECORD_UUID_CREDENTIAL = "auth_record_uuid"
    }
}
