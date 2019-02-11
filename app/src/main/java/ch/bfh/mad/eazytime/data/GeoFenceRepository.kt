package ch.bfh.mad.eazytime.data

import android.arch.lifecycle.MutableLiveData
import ch.bfh.mad.eazytime.data.entity.GeoFence
import javax.inject.Inject

class GeoFenceRepository @Inject constructor() {

    fun saveGeoFence(geoFence: GeoFence) {
        // TODO implement saveGeoFence
    }

    fun loadGeoFences(): MutableLiveData<List<GeoFence>> {
        val geoFenceList : MutableLiveData<List<GeoFence>> = MutableLiveData()

        return geoFenceList
    }
}