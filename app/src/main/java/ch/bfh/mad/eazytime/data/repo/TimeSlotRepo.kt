package ch.bfh.mad.eazytime.data.repo

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import ch.bfh.mad.eazytime.data.dao.TimeSlotDao
import ch.bfh.mad.eazytime.data.entity.TimeSlot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate
import org.joda.time.LocalTime

class TimeSlotRepo(private val timeSlotDao: TimeSlotDao) {

    val allTimeSlots: LiveData<List<TimeSlot>> = timeSlotDao.getTimeSlots()

    @WorkerThread
    fun todaysTimeSlots(): LiveData<List<TimeSlot>> = runBlocking {
        timeSlotDao.getTimeSlotsForDay(LocalDate().toLocalDateTime(LocalTime.MIDNIGHT))
    }

    @WorkerThread
    fun todaysTimeSlotsList(): List<TimeSlot> = runBlocking {
        timeSlotDao.getTimeSlotsListForDay(LocalDate().toLocalDateTime(LocalTime.MIDNIGHT))
    }

    @WorkerThread
    suspend fun insertAll(timeSlots: List<TimeSlot>) = withContext(Dispatchers.IO) {
        timeSlotDao.insertAll(timeSlots)
    }

    @WorkerThread
    suspend fun update(timeSlot: TimeSlot)= withContext(Dispatchers.IO) {
        timeSlotDao.update(timeSlot)
    }

    @WorkerThread
    suspend fun delete(timeSlot: TimeSlot) = withContext(Dispatchers.IO) {
        timeSlotDao.delete(timeSlot)
    }

    @WorkerThread
    suspend fun getCurrentTimeSlots(): List<TimeSlot> = withContext(Dispatchers.IO) {
        timeSlotDao.getCurrentTimeSlots()
    }

    @WorkerThread
    suspend fun getTimeSlotsByWorkDayId(workDayId: Long): List<TimeSlot> = withContext(Dispatchers.IO)  {
        timeSlotDao.getTimeSlotsByWorkDayId(workDayId)
    }
}