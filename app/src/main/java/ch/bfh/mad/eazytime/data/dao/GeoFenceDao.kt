package ch.bfh.mad.eazytime.data.dao

import androidx.room.*
import ch.bfh.mad.eazytime.data.entity.GeoFence

@Dao
interface GeoFenceDao {
    @Query("SELECT * FROM geofence")
    fun getGeoFences(): List<GeoFence>

    @Insert
    fun insertAll(geoFences: List<GeoFence>)

    @Update
    fun update(geoFence: GeoFence)

    @Delete
    fun delete(geoFence: GeoFence)
}