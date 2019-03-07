package ch.bfh.mad.eazytime.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.data.entity.WorkDay

class CalendarListAdapter(context: Context, @LayoutRes itemLayoutRes: Int) :
    ArrayAdapter<WorkDay>(context, itemLayoutRes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_calendar, parent, false)
        getItem(position)?.let { calendarListItem ->
            val nameTV = view.findViewById<TextView>(R.id.calendar_list_item_date_name)
            nameTV.text = calendarListItem.id.toString()
            //todo
        }
        return view
    }
}