package ch.bfh.mad.eazytime.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity
data class GeoFence(
    val name: String,
    val active: Boolean = false,
    val gfId: String,
    val radius: Double,
    val position: LatLng,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
)
