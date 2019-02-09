package ch.bfh.mad.eazytime.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
    ForeignKey(
        entity = Project::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("project_id")),
    ForeignKey(
        entity = WorkDay::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("workday_id"))))
data class TimeSlot(@PrimaryKey(autoGenerate = true)
                    var id: Long = 0,
                    var startDate: Long? = null,
                    var endDate: Long? = null,
                    @ColumnInfo(name = "project_id")
                    var projectId: Long? = null,
                    @ColumnInfo(name = "workday_id")
                    var workDayId: Long? = null)