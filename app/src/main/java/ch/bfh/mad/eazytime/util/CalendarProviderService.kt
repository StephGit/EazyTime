package ch.bfh.mad.eazytime.util

import androidx.lifecycle.LiveData
import ch.bfh.mad.eazytime.data.dao.WorkDayDao
import ch.bfh.mad.eazytime.data.entity.TimeSlot
import ch.bfh.mad.eazytime.data.entity.WorkDayAndTimeSlots
import ch.bfh.mad.eazytime.data.repo.TimeSlotRepo
import ch.bfh.mad.eazytime.data.repo.WorkDayRepo
import kotlinx.coroutines.*

class CalendarProviderService(private val workDayRepo: WorkDayRepo, val timeSlotRepo: TimeSlotRepo) {

    suspend fun getCalendarListitems(): List<WorkDayAndTimeSlots> = withContext(Dispatchers.IO) {
        workDayRepo.getWorkDays()
    }

    suspend fun getCalendarDetail(id: Long): List<WorkDayDao.WorkDayTimeSlotProject> = withContext(Dispatchers.IO) {
        workDayRepo.getWorkDayTimeSlotProject(id)
    }

    suspend fun getTimeSlotsByWorkDayId(workdayId: Long): List<TimeSlot> = withContext(Dispatchers.IO) {
        timeSlotRepo.getTimeSlotsByWorkDayId(workdayId)
    }
}