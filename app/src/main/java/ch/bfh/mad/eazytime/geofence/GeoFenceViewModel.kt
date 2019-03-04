package ch.bfh.mad.eazytime.geofence

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ch.bfh.mad.eazytime.data.GeoFenceRepository
import ch.bfh.mad.eazytime.data.entity.GeoFence
import javax.inject.Inject

class GeoFenceViewModel @Inject constructor(var geoFenceRepository: GeoFenceRepository) : ViewModel() {

    var hasGeoFence = geoFenceRepository.hasGeoFence()

    val geoFenceItems: LiveData<List<GeoFence>> = geoFenceRepository.geoFences

    fun insert(geoFence: GeoFence) {
        geoFenceRepository.insert(geoFence)
    }

    fun delete(geoFence: GeoFence) {
        geoFenceRepository.delete(geoFence)
    }

    fun update(geoFence: GeoFence) {
        geoFenceRepository.update(geoFence)
    }

}