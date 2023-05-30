package ru.virgil.spring_tools.tools.security.record

import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthRecordService {

    private val authRecords: MutableMap<UUID, AuthRecord> = HashMap()

    fun getAuthRecord(uuid: UUID): AuthRecord? = authRecords[uuid]

    fun createAuthRecord(): AuthRecord {
        val authRecord = AuthRecord()
        authRecords[authRecord.uuid] = authRecord
        return authRecord
    }

    fun editAuthRecord(uuid: UUID, authRecord: AuthRecord): AuthRecord {
        authRecords[uuid] = authRecord
        return authRecord
    }

    fun deleteAuthRecord(uuid: UUID) {
        authRecords.remove(uuid)
    }
}
