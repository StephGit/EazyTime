package ch.bfh.mad.eazytime.calendar.detail

import androidx.lifecycle.ViewModel
import ch.bfh.mad.eazytime.data.dao.WorkDayDao
import ch.bfh.mad.eazytime.data.entity.TimeSlot
import ch.bfh.mad.eazytime.util.CalendarProviderService
import kotlinx.coroutines.*
import javax.inject.Inject

class CalendarDetailViewModel @Inject constructor(private val calendarProviderService: CalendarProviderService): ViewModel() {

    fun getCalendarDetailAsync(id: Long): Deferred<List<WorkDayDao.WorkDayTimeSlotProject>> = GlobalScope.async {
        calendarProviderService.getCalendarDetail(id)
    }

    fun getTimeSlotListAsync(workdayId: Long): Deferred<List<TimeSlot>> = GlobalScope.async {
        calendarProviderService.getTimeSlotsByWorkDayId(workdayId)
    }
}