package ch.bfh.mad.eazytime.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import ch.bfh.mad.eazytime.R
import ch.bfh.mad.eazytime.data.entity.WorkDayAndTimeSlots
import ch.bfh.mad.eazytime.util.CalendarUtil
import org.joda.time.format.DateTimeFormat
import java.util.*

class CalendarListAdapter(context: Context, @LayoutRes itemLayoutRes: Int) :
    ArrayAdapter<WorkDayAndTimeSlots>(context, itemLayoutRes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_calendar, parent, false)
        getItem(position)?.let { calendarListItem ->
            val nameTV = view.findViewById<TextView>(R.id.calendar_list_item_date_name)
            val locale = Locale(context.getString(R.string.language))
            val pattern = DateTimeFormat.forPattern(context.getString(R.string.date_pattern)).withLocale(locale)
            nameTV.text = pattern.print(calendarListItem.workDay.date)

            val totalHoursTv = view.findViewById<TextView>(R.id.calendar_list_item_total_hours)
            val periodOfTotalWorkHours = CalendarUtil.getPeriodOfTotalWorkHours(calendarListItem.timeslots)
            val hoursAndMinutesFormatter = CalendarUtil.getHoursAndMinutesFormatter()

            totalHoursTv.text = hoursAndMinutesFormatter.print(periodOfTotalWorkHours.normalizedStandard())
        }
        return view
    }
}