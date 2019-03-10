package ch.bfh.mad.eazytime.calendar


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import ch.bfh.mad.R
import ch.bfh.mad.eazytime.EazyTimeNavigator
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.entity.WorkDay
import ch.bfh.mad.eazytime.di.Injector
import javax.inject.Inject

class CalendarFragment : androidx.fragment.app.Fragment() {

    private lateinit var calendarListView: ListView
    private lateinit var calendarListViewModel: CalendarListViewModel
    private lateinit var navigator: EazyTimeNavigator

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        navigator = requireContext() as? EazyTimeNavigator ?: throw IllegalStateException("Context of ProjectFragment is not an Instance of EazyTimeNavigator")
        calendarListView = view.findViewById(R.id.lv_calendar)
        activity!!.title = getString(R.string.calendar_fragment_title)

        Injector.appComponent.inject(this)
        calendarListViewModel = ViewModelProviders.of(this, viewModelFactory).get(CalendarListViewModel::class.java)
        val calendarListAdapter = CalendarListAdapter(requireContext(), android.R.layout.simple_list_item_1)
        calendarListView.adapter = calendarListAdapter
        calendarListView.setOnItemClickListener { _, _, position, _ ->
            openCalendarDetailActivity(calendarListAdapter.getItem(position))
        }
        calendarListViewModel.getCalendarItems().observe(this, Observer { calendarItems ->
            calendarItems?.let {
                calendarListAdapter.clear()
                calendarListAdapter.addAll(it)
            }
        })

        return view
    }

    private fun openCalendarDetailActivity(calendarListItem: WorkDay?) {
        calendarListItem?.let { listItem ->
            Log.i(TAG, "Start openCalendarDetailActivity for $listItem")
            navigator.openCalendarDetailActivity(listItem.id)
        }
    }


}
