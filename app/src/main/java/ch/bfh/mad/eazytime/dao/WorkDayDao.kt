package ch.bfh.mad.eazytime.dao

import android.arch.persistence.room.*
import ch.bfh.mad.eazytime.entity.WorkDay

@Dao
interface WorkDayDao {
    @Query("SELECT * FROM workday")
    fun getWorkDays(): List<WorkDay>

    @Insert
    fun insertAll(workDays: List<WorkDay>)

    @Update
    fun update(workDay: WorkDay)

    @Delete
    fun delete(workDay: WorkDay)
}