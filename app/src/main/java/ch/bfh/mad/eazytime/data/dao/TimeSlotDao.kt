package ch.bfh.mad.eazytime.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ch.bfh.mad.eazytime.data.entity.TimeSlot
import ch.bfh.mad.eazytime.data.entity.WorkDay
import org.joda.time.LocalDateTime

@Dao
interface TimeSlotDao {
    @Query("SELECT * FROM timeslot")
    fun getTimeSlots(): LiveData<List<TimeSlot>>

    @Insert
    fun insertAll(timeSlots: List<TimeSlot>)

    @Update
    fun update(timeSlot: TimeSlot)

    @Delete
    fun delete(timeSlot: TimeSlot)

    @Query("SELECT * FROM timeslot WHERE endDate is NULL")
    fun getCurrentTimeSlots(): List<TimeSlot>

    @Query("SELECT * FROM timeslot where startDate > :startOfDay")
    fun getTimeSlotsForDay(startOfDay: LocalDateTime?): LiveData<List<TimeSlot>>

    @Query("SELECT * FROM timeslot where workday_id = :workDayId")
    fun getTimeSlotsByWorkDayId(workDayId: Long): List<TimeSlot>
}