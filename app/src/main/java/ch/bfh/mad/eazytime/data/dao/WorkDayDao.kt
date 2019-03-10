package ch.bfh.mad.eazytime.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ch.bfh.mad.eazytime.data.entity.WorkDay
import ch.bfh.mad.eazytime.data.entity.WorkDayAndTimeSlots
import org.joda.time.LocalDate

@Dao
interface WorkDayDao {
    @Query("SELECT * FROM workday ORDER BY date ASC")
    fun getWorkDays(): LiveData<List<WorkDay>>

    @Insert
    fun insertAll(workDays: List<WorkDay>)

    @Insert
    fun insert(workDay: WorkDay): Long

    @Update
    fun update(workDay: WorkDay)

    @Delete
    fun delete(workDay: WorkDay)

    @Query("SELECT * FROM workday WHERE date == :date")
    fun getWorkDayByDate(date: LocalDate): WorkDay?

    @Query("SELECT * FROM workday WHERE id == :id")
    fun getWorkDayAndTimeSlotsById(id: Long): WorkDayAndTimeSlots?

    @Query("SELECT " +
            "workday.id AS workDayId, " +
            "workday.date as workDayDate, " +
            "timeslot.id AS timeSlotId, " +
            "project.id AS projectId " +
            "FROM workday " +
            "INNER JOIN timeslot ON timeslot.workday_id = workday.id " +
            "INNER JOIN project ON project.id = timeslot.project_id " +
            "WHERE workday.id == :id")
    fun getWorkDayTimeSlotProject(id: Long): List<WorkDayTimeSlotProject>

    data class WorkDayTimeSlotProject(
        var workDayId: Long?,
        var workDayDate: LocalDate?,
        var timeSlotId: Long?,
        var projectId: Long?)
}