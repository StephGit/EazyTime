package ch.bfh.mad.eazytime.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.bfh.mad.eazytime.data.entity.WorkDayAndTimeSlots
import ch.bfh.mad.eazytime.util.CalendarProviderService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class CalendarListViewModel @Inject constructor(private val calendarProviderService: CalendarProviderService): ViewModel() {

    private val _calendarItems: MutableLiveData<List<WorkDayAndTimeSlots>> = MutableLiveData()

    fun getCalendarItems(): LiveData<List<WorkDayAndTimeSlots>> {
        GlobalScope.launch {
            _calendarItems.postValue(calendarProviderService.getCalendarListitems())
            calendarProviderService.getCalendarListitems()
        }
        return _calendarItems

    }
}