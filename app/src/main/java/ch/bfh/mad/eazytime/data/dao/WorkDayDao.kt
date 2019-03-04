package ch.bfh.mad.eazytime.data.dao

import androidx.room.*
import ch.bfh.mad.eazytime.data.entity.WorkDay
import org.joda.time.LocalDate

@Dao
interface WorkDayDao {
    @Query("SELECT * FROM workday")
    fun getWorkDays(): List<WorkDay>

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
}