package ch.bfh.mad.eazytime.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.joda.time.LocalDate

@Entity
data class WorkDay(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var date: LocalDate? = null,
    var totalWorkHours: Float? = null)

class WorkDayAndTimeSlots{
    @Embedded
    var workDay: WorkDay = WorkDay()
    @Relation(parentColumn = "id", entityColumn = "workday_id")
    var timeslots: List<TimeSlot> = listOf()
}
