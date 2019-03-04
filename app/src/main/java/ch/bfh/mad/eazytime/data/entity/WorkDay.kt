package ch.bfh.mad.eazytime.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.LocalDate

@Entity
data class WorkDay(@PrimaryKey(autoGenerate = true)
                   var id: Long = 0,
                   var date: LocalDate? = null,
                   var totalWorkHours: Float? = null)
