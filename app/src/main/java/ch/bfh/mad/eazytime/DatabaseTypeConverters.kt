package ch.bfh.mad.eazytime

import android.arch.persistence.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class DatabaseTypeConverters {

    @TypeConverter
    fun longToDateTime(v: Long): LocalDateTime {
        return Instant.ofEpochMilli(v).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    @TypeConverter
    fun dateTimeToLong(v: LocalDateTime): Long {
        return v.atZone(ZoneId.systemDefault()).toEpochSecond()
    }

    @TypeConverter
    fun longToDate(v: Long): LocalDate {
        return Instant.ofEpochMilli(v).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    @TypeConverter
    fun dateToLong(v: LocalDate): Long {
        return v.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
    }
}