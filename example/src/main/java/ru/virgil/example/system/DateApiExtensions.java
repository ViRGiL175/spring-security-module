package ru.virgil.example.system;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateApiExtensions {

    public static final ChronoUnit THRESHOLD = ChronoUnit.MILLIS;

    /**
     * Округляет дату до секунд. Позволяет избежать бага с округлением времени в базе данных.
     * <p>
     * todo: С каждым выходом Spring надо проверять, не исправилось ли. Обещали исправить.
     *  Для этого заменяем {@link ChronoUnit#MILLIS} на {@link ChronoUnit#NANOS}.
     */
    @Nullable
    public static LocalDateTime databaseTruncate(@Nullable LocalDateTime original) {
        return original == null ? null : original.truncatedTo(THRESHOLD);
    }
}
