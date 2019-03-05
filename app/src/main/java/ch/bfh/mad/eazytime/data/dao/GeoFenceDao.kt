package ch.bfh.mad.eazytime.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ch.bfh.mad.eazytime.data.entity.GeoFence

@Dao
interface GeoFenceDao {
    @Query("SELECT * FROM geofence LIMIT 1")
    fun getAnyGeoFence(): GeoFence?

    @Query("SELECT * FROM geofence where active = 1")
    fun getActiveGeoFences(): List<GeoFence>

    @Query("SELECT * FROM geofence")
    fun getGeoFences(): LiveData<List<GeoFence>>

    @Insert
    fun insert(geoFence: GeoFence): Long

    @Update
    fun update(geoFence: GeoFence)

    @Delete
    fun delete(geoFence: GeoFence)
}