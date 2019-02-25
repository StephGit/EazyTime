package ch.bfh.mad.eazytime.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import androidx.room.*
import ch.bfh.mad.eazytime.data.entity.GeoFence

@Dao
interface GeoFenceDao {
    @Query("SELECT * FROM geofence")
    fun getGeoFences(): LiveData<List<GeoFence>>

    @Insert
    fun insert(geoFence: GeoFence): Long

    @Update
    fun update(geoFence: GeoFence)

    @Delete
    fun delete(geoFence: GeoFence)
}