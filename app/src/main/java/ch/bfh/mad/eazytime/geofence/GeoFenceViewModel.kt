package ch.bfh.mad.eazytime.geofence

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ch.bfh.mad.eazytime.data.GeoFenceRepository
import ch.bfh.mad.eazytime.data.entity.GeoFence
import ch.bfh.mad.eazytime.di.Injector
import javax.inject.Inject

class GeoFenceViewModel: ViewModel() {

    @Inject
    lateinit var geofenceRepository: GeoFenceRepository

    val geoFenceItems: MutableLiveData<List<GeoFence>>

    init {
        Injector.appComponent.inject(this)
        geoFenceItems = geofenceRepository.loadGeoFences()
    }
}