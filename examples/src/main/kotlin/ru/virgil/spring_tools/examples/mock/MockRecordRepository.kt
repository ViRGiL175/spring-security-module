package ru.virgil.spring_tools.examples.mock

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.virgil.spring_tools.examples.system.entity.OwnedRepository
import java.util.*

@Repository
interface MockRecordRepository : CrudRepository<MockRecord, UUID>, OwnedRepository<MockRecord>
