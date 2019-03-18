package ch.bfh.mad.eazytime.util

import ch.bfh.mad.eazytime.data.entity.TimeSlot
import org.joda.time.Period
import org.joda.time.PeriodType
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
            .appendLiteral(":")
            .appendSeconds()
            .toFormatter()
    }


    fun getPeriodOfTotalWorkHours(timeSlots: List<TimeSlot>): Period {
       return  timeSlots.filter { timeSlot -> timeSlot.endDate != null}
            .map { timeSlot ->
                Period(timeSlot.startDate, timeSlot.endDate, PeriodType.standard().withMillisRemoved())
            }
            .fold(Period(0L)) { acc, period ->  acc.plus(period)}
    }
}