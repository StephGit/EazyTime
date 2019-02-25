package ch.bfh.mad.eazytime.geofence

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import ch.bfh.mad.eazytime.data.GeoFenceRepository
import ch.bfh.mad.eazytime.data.entity.GeoFence
import ch.bfh.mad.eazytime.di.Injector
import javax.inject.Inject

class GeoFenceViewModel @Inject constructor(geofenceRepository: GeoFenceRepository) : ViewModel() {

    val geoFenceItems: LiveData<List<GeoFence>>

    init {
        Injector.appComponent.inject(this)
        geoFenceItems = geofenceRepository.loadGeoFences()
    }
}