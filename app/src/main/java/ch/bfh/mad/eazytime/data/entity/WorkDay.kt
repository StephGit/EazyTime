package ch.bfh.mad.eazytime.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.joda.time.LocalDate

@Entity
data class WorkDay(@PrimaryKey(autoGenerate = true)
                   var id: Long = 0,
                   var date: LocalDate? = null,
                   var totalWorkHours: Float? = null)
