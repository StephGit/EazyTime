package ch.bfh.mad.eazytime.data

import android.arch.lifecycle.LiveData
import android.util.Log
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.dao.GeoFenceDao
import ch.bfh.mad.eazytime.data.entity.GeoFence
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GeoFenceRepository @Inject constructor(geoFenceDao: GeoFenceDao) {

    var geoFenceDao: GeoFenceDao = geoFenceDao

    val geoFences: LiveData<List<GeoFence>> = geoFenceDao.getGeoFences()

    fun saveGeoFence(geoFence: GeoFence) {
        Observable.fromCallable {
            geoFenceDao.insert(geoFence)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                Log.i(TAG, "Inserted Geofence to db with id: " + it)
            }
    }


}