package ch.bfh.mad.eazytime.util

import androidx.lifecycle.LiveData
import ch.bfh.mad.eazytime.data.dao.WorkDayDao
import ch.bfh.mad.eazytime.data.entity.TimeSlot
import ch.bfh.mad.eazytime.data.entity.WorkDayAndTimeSlots
import ch.bfh.mad.eazytime.data.repo.TimeSlotRepo
import ch.bfh.mad.eazytime.data.repo.WorkDayRepo
import kotlinx.coroutines.runBlocking

class CalendarProviderService(val workDayRepo: WorkDayRepo, val timeSlotRepo: TimeSlotRepo) {

    fun getCalendarListitems(): LiveData<List<WorkDayAndTimeSlots>> = runBlocking {
        return@runBlocking workDayRepo.getWorkDays()
    }

    fun getCalendarDetail(id: Long): List<WorkDayDao.WorkDayTimeSlotProject> = runBlocking {
        return@runBlocking workDayRepo.getWorkDayTimeSlotProject(id)
    }

    fun getTimeSlotsByWorkDayId(workdayId: Long): List<TimeSlot> = runBlocking {
        return@runBlocking timeSlotRepo.getTimeSlotsByWorkDayId(workdayId)
    }
}