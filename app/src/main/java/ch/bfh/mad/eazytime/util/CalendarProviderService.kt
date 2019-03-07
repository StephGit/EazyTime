package ch.bfh.mad.eazytime.util

import androidx.lifecycle.LiveData
import ch.bfh.mad.eazytime.data.entity.WorkDay
import ch.bfh.mad.eazytime.data.repo.TimeSlotRepo
import ch.bfh.mad.eazytime.data.repo.WorkDayRepo
import kotlinx.coroutines.runBlocking

class CalendarProviderService(val workDayRepo: WorkDayRepo, val timeSlotRepo: TimeSlotRepo) {

    fun getCalendarListitems(): LiveData<List<WorkDay>> = runBlocking {
        return@runBlocking workDayRepo.getWorkDays()
    }
}