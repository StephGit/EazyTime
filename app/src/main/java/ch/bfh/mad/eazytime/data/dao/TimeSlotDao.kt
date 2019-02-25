package ch.bfh.mad.eazytime.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import ch.bfh.mad.eazytime.data.entity.TimeSlot

@Dao
interface TimeSlotDao {
    @Query("SELECT * FROM timeslot")
    fun getTimeSlots(): List<TimeSlot>

    @Insert
    fun insertAll(timeSlots: List<TimeSlot>)

    @Update
    fun update(timeSlot: TimeSlot)

    @Delete
    fun delete(timeSlot: TimeSlot)

    @Query("SELECT * FROM timeslot WHERE endDate is NULL")
    fun getCurrentTimeSlots(): List<TimeSlot>

    @Query("SELECT * FROM timeslot")
    fun getTimeSlotsLiveData(): LiveData<List<TimeSlot>>
}