package ch.bfh.mad.eazytime.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GeoFence(@PrimaryKey(autoGenerate = true)
                    var id: Long = 0,
                    var name: String? = null,
                    var active: Boolean? = null,
                    var longitude: Float? = null,
                    var latitude: Float? = null,
                    var radius: Int? = null,
                    var gfId: Long? = null)
