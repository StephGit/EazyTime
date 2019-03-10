package ch.bfh.mad.eazytime.calendar.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.data.dao.WorkDayDao

class CalendarDetailListAdapter(context: Context, @LayoutRes itemLayoutRes: Int) :
    ArrayAdapter<WorkDayDao.WorkDayTimeSlotProject>(context, itemLayoutRes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_calendar_detail, parent, false)
        getItem(position)?.let { calendarListItem ->


        }
        return view
    }
}