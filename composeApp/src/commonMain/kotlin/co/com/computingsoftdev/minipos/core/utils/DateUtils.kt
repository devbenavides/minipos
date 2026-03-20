package co.com.computingsoftdev.minipos.core.utils

import kotlinx.datetime.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object DateUtils {

    private val timeZone = TimeZone.currentSystemDefault()


    fun now(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    private fun nowInstant(): Instant {
        return Instant.fromEpochMilliseconds(
            Clock.System.now().toEpochMilliseconds()
        )
    }

    fun startOfToday(): Long {
        val today = nowInstant()
            .toLocalDateTime(timeZone)
            .date

        val start = today.atStartOfDayIn(timeZone)
        return start.toEpochMilliseconds()
    }

    fun startOfWeek(): Long {
        val today = nowInstant()
            .toLocalDateTime(timeZone)
            .date

        val firstDay = today.minus(today.dayOfWeek.ordinal, DateTimeUnit.DAY)

        return firstDay.atStartOfDayIn(timeZone).toEpochMilliseconds()
    }

    fun startOfMonth(): Long {
        val today = nowInstant()
            .toLocalDateTime(timeZone)
            .date

        val firstDay = LocalDate(today.year, today.month, 1)

        return firstDay.atStartOfDayIn(timeZone).toEpochMilliseconds()
    }
}