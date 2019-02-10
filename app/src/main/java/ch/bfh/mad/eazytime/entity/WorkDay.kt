package ch.bfh.mad.eazytime.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Relation
import java.time.LocalDate

@Entity
data class WorkDay(@PrimaryKey(autoGenerate = true)
                   var id: Long = 0,
                   var date: LocalDate? = null,
                   var totalWorkHours: Float? = null)
