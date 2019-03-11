package ch.bfh.mad.eazytime.calendar.detail

import androidx.lifecycle.ViewModel
import ch.bfh.mad.eazytime.data.dao.WorkDayDao
import ch.bfh.mad.eazytime.util.CalendarProviderService
import javax.inject.Inject

class CalendarDetailViewModel @Inject constructor(private val calendarProviderService: CalendarProviderService): ViewModel() {

    fun getCalendarDetail(id: Long): List<WorkDayDao.WorkDayTimeSlotProject> {
        return calendarProviderService.getCalendarDetail(id)
    }
}