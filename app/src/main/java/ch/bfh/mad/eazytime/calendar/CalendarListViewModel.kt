package ch.bfh.mad.eazytime.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ch.bfh.mad.eazytime.data.entity.WorkDayAndTimeSlots
import ch.bfh.mad.eazytime.util.CalendarProviderService
import javax.inject.Inject

class CalendarListViewModel @Inject constructor(val calendarProviderService: CalendarProviderService): ViewModel() {

    fun getCalendarItems(): LiveData<List<WorkDayAndTimeSlots>> {
        return calendarProviderService.getCalendarListitems()
    }
}