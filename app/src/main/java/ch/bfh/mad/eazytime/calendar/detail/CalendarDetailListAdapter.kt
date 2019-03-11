package ch.bfh.mad.eazytime.calendar.detail

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.data.dao.WorkDayDao
import ch.bfh.mad.eazytime.util.CalendarUtil
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat

class CalendarDetailListAdapter(context: Context, @LayoutRes itemLayoutRes: Int) :
    ArrayAdapter<WorkDayDao.WorkDayTimeSlotProject>(context, itemLayoutRes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_calendar_detail, parent, false)
        getItem(position)?.let { calendarListItem ->
            val colorTagTv = view.findViewById<TextView>(R.id.calendar_detail_list_item_color_tag)
            colorTagTv.setBackgroundColor(Color.parseColor(calendarListItem.projectColor))
            colorTagTv.text = calendarListItem.projectShortCode?.toUpperCase()

            val timeSlotTv = view.findViewById<TextView>(R.id.calendar_detail_list_item_timeslot)
            val pattern = DateTimeFormat.forPattern("HH:mm")
            val startDate = pattern.print(calendarListItem.timeSlotStartDate)
            val endDate = calendarListItem.timeSlotEndDate?.let {
                pattern.print(it)
            } ?: context.getString(R.string.now)

            val timeRangePattern = context.getString(R.string.time_range_pattern)
            timeSlotTv.text = String.format(timeRangePattern, startDate, endDate)

            val durationTv = view.findViewById<TextView>(R.id.calendar_detail_list_item_duration)
            val hoursAndMinutesFormatter = CalendarUtil.getHoursAndMinutesFormatter()
            val periodStr = calendarListItem.timeSlotEndDate?.let { timeSlotEndDate ->
                val period = Period(calendarListItem.timeSlotStartDate, timeSlotEndDate)
                hoursAndMinutesFormatter.print(period)
            } ?: context.getString(R.string.ongoing)

            durationTv.text = periodStr

        }
        return view
    }
}