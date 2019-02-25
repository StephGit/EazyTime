package ch.bfh.mad.eazytime.data

import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import android.util.Log
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.dao.GeoFenceDao
import ch.bfh.mad.eazytime.data.entity.GeoFence
import javax.inject.Inject

class GeoFenceRepository @Inject constructor(geoFenceDao: GeoFenceDao) {

    var geoFenceDao: GeoFenceDao = geoFenceDao
    val geoFences: LiveData<List<GeoFence>> = geoFenceDao.getGeoFences()

    fun hasGeoFence(): Boolean {
        return GetAsyncTask(geoFenceDao).execute().get()
    }

    fun saveGeoFence(geoFence: GeoFence) {
        InsertAsyncTask(geoFenceDao).execute(geoFence)
    }

    fun updateGeoFenceItem(geoFence: GeoFence) {
        geoFenceDao.update(geoFence)
    }

    private class InsertAsyncTask internal constructor(private val geoFenceDao: GeoFenceDao) :
        AsyncTask<GeoFence, Void, Void>() {
        override fun doInBackground(vararg params: GeoFence): Void? {
            val geoFence = params[0]
            Log.i(TAG, "InsertAsyncTask")
            geoFenceDao.insert(geoFence)
            return null
        }
    }

    private class GetAsyncTask internal constructor(private val geoFenceDao: GeoFenceDao) :
        AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void): Boolean {
            Log.i(TAG, "GetAsyncTask")
            return (geoFenceDao.getAnyGeoFence() != null)
        }
    }
}