package ch.bfh.mad.eazytime.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GeoFence(
    val name: String,
    val active: Boolean = false,
    val gfId: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
)
