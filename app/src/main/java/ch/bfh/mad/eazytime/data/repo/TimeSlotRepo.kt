package ch.bfh.mad.eazytime.data.repo

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import ch.bfh.mad.eazytime.data.dao.TimeSlotDao
import ch.bfh.mad.eazytime.data.entity.TimeSlot

@Suppress("RedundantSuspendModifier")
class TimeSlotRepo(private val timeSlotDao: TimeSlotDao) {

    val allTimeSlots: LiveData<List<TimeSlot>> = timeSlotDao.getTimeSlots()

    @WorkerThread
    suspend fun insertAll(timeSlots: List<TimeSlot>) {
        timeSlotDao.insertAll(timeSlots)
    }

    @WorkerThread
    suspend fun update(timeSlot: TimeSlot) {
        timeSlotDao.update(timeSlot)
    }

    @WorkerThread
    suspend fun delete(timeSlot: TimeSlot) {
        timeSlotDao.delete(timeSlot)
    }

    @WorkerThread
    suspend fun getCurrentTimeSlots(): List<TimeSlot> {
        return timeSlotDao.getCurrentTimeSlots()
    }

}