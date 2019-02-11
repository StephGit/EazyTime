package ch.bfh.mad.eazytime.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class GeoFence(@PrimaryKey(autoGenerate = true)
                    var id: Long = 0,
                    var name: String? = null,
                    var longitude: Float? = null,
                    var latitude: Float? = null,
                    var radius: Int? = null,
                    var gfId: Long? = null)
