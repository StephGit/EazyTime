package ch.bfh.mad.eazytime.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GeoFence(
    var name: String?,
    var active: Boolean = false,
    var gfId: String?,
    var radius: Double?,
    var latitude: Double?,
    var longitude: Double?,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
)
