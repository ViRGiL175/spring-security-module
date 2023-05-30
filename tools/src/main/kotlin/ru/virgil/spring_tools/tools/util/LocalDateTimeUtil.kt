package ru.virgil.spring_tools.tools.util

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object LocalDateTimeUtil {

    private val THRESHOLD = ChronoUnit.MILLIS

    /**
     * Округляет дату до секунд. Позволяет избежать бага с округлением времени в базе данных.
     *
     * todo: С каждым выходом Spring надо проверять, не исправилось ли. Обещали исправить.
     * Для этого заменяем [ChronoUnit.MILLIS] на [ChronoUnit.NANOS].
     */
    fun LocalDateTime.databaseTruncate(): LocalDateTime = this.truncatedTo(THRESHOLD)
}
