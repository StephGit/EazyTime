package ch.bfh.mad.eazytime.data.repo

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import ch.bfh.mad.eazytime.data.dao.TimeSlotDao
import ch.bfh.mad.eazytime.data.entity.TimeSlot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TimeSlotRepo(private val timeSlotDao: TimeSlotDao) {

    val allTimeSlots: LiveData<List<TimeSlot>> = timeSlotDao.getTimeSlots()

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

}