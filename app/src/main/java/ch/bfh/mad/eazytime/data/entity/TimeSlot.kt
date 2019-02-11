package ch.bfh.mad.eazytime.data.entity

import android.arch.persistence.room.*
import org.joda.time.LocalDateTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("project_id")),
        ForeignKey(
            entity = WorkDay::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("workday_id"))],
    indices = [
        Index(value = ["project_id"]),
        Index(value = ["workday_id"])
    ])
data class TimeSlot(@PrimaryKey(autoGenerate = true)
                    var id: Long = 0,
                    var startDate: LocalDateTime? = null,
                    var endDate: LocalDateTime? = null,
                    @ColumnInfo(name = "project_id")
                    var projectId: Long? = null,
                    @ColumnInfo(name = "workday_id")
                    var workDayId: Long? = null)