package ch.bfh.mad.eazytime.calendar


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ch.bfh.mad.R

class CalendarFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        val textView = view.findViewById<TextView>(R.id.tv_calendar_placeholder)
        textView.text = getString(R.string.placeholder)+ " Calendar"
        activity!!.title = getString(R.string.calendar_fragment_title)
        return view
    }


}
