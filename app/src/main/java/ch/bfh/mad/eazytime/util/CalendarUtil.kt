package ch.bfh.mad.eazytime.util

import org.joda.time.format.PeriodFormatter
import org.joda.time.format.PeriodFormatterBuilder

object CalendarUtil {

     fun getHoursAndMinutesFormatter(): PeriodFormatter {
        return PeriodFormatterBuilder()
            .printZeroAlways()
            .minimumPrintedDigits(2)
            .appendHours()
            .appendLiteral(":")
            .appendMinutes()
            .toFormatter()
    }
}