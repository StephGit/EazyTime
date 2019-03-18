package ch.bfh.mad.eazytime.geofence

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ch.bfh.mad.eazytime.data.entity.GeoFence
import ch.bfh.mad.eazytime.data.repo.GeoFenceRepo
import javax.inject.Inject

class GeoFenceViewModel @Inject constructor(var geoFenceRepo: GeoFenceRepo) : ViewModel() {

    val geoFenceItems: LiveData<List<GeoFence>> = geoFenceRepo.geoFences

    fun insert(geoFence: GeoFence) {
        geoFenceRepo.insert(geoFence)
    }

    fun delete(geoFence: GeoFence) {
        geoFenceRepo.delete(geoFence)
    }

    fun update(geoFence: GeoFence) {
        geoFenceRepo.update(geoFence)
    }

}