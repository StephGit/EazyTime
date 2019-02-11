package ch.bfh.mad.eazytime.data

import android.arch.persistence.room.TypeConverter
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime


class DatabaseTypeConverters {

    @TypeConverter
    fun longToDateTime(v: Long): LocalDateTime {
        return LocalDateTime(v)
    }

    @TypeConverter
    fun dateTimeToLong(v: LocalDateTime): Long {
        return v.toDateTime().millis
    }

    @TypeConverter
    fun longToDate(v: Long): LocalDate {
        return LocalDate(v)
    }

    @TypeConverter
    fun dateToLong(v: LocalDate): Long {
        return v.toDateTimeAtStartOfDay().toDateTime().millis
    }
}