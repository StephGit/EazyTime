package ch.bfh.mad.eazytime.geofence

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import ch.bfh.mad.eazytime.data.GeoFenceRepository
import ch.bfh.mad.eazytime.data.entity.GeoFence
import javax.inject.Inject

class GeoFenceViewModel @Inject constructor(geofenceRepository: GeoFenceRepository) : ViewModel() {

    var hasGeoFenceInDatabase = geofenceRepository.hasGeoFence()

    val geoFenceItems: LiveData<List<GeoFence>> = geofenceRepository.geoFences

}