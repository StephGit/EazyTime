package ch.bfh.mad.eazytime.data.repo

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import ch.bfh.mad.eazytime.data.dao.GeoFenceDao
import ch.bfh.mad.eazytime.data.entity.GeoFence
import javax.inject.Inject

class GeoFenceRepo @Inject constructor(private var geoFenceDao: GeoFenceDao) {

    var geoFences: LiveData<List<GeoFence>> = geoFenceDao.getGeoFences()

    fun hasGeoFence(): Boolean {
        return GetAsyncTask(geoFenceDao).execute().get()
    }

    fun getActiveGeoFences(): List<GeoFence> {
        return GetActiveAsyncTask(geoFenceDao).execute().get()
    }

    fun getGeoFence(geoFenceId: Long): GeoFence? {
        return GetByIdAsyncTask(geoFenceDao).execute(geoFenceId).get()
    }

    fun insert(geoFence: GeoFence) {
        InsertAsyncTask(geoFenceDao).execute(geoFence)
    }

    fun update(geoFence: GeoFence) {
        UpdateAsyncTask(geoFenceDao).execute(geoFence)
    }

    fun delete(geoFence: GeoFence) {
        DeleteAsyncTask(geoFenceDao).execute(geoFence)
    }

    private class InsertAsyncTask internal constructor(private val geoFenceDao: GeoFenceDao) :
        AsyncTask<GeoFence, Void, Void>() {
        override fun doInBackground(vararg params: GeoFence): Void? {
            geoFenceDao.insert(params[0])
            return null
        }
    }

    private class GetAsyncTask internal constructor(private val geoFenceDao: GeoFenceDao) :
        AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void): Boolean {
            return (geoFenceDao.getAnyGeoFence() != null)
        }
    }

    private class GetActiveAsyncTask internal constructor(private val geoFenceDao: GeoFenceDao) :
        AsyncTask<Void, Void, List<GeoFence>>() {
        override fun doInBackground(vararg params: Void):  List<GeoFence> {
            return (geoFenceDao.getActiveGeoFences())
        }
    }

    private class GetByIdAsyncTask internal constructor(private val geoFenceDao: GeoFenceDao) :
        AsyncTask<Long, Void, GeoFence?>() {
        override fun doInBackground(vararg params: Long?): GeoFence? {
            return (geoFenceDao.getGeoFenceById(params[0]!!))
        }
    }

    private class UpdateAsyncTask internal constructor(private val geoFenceDao: GeoFenceDao) :
        AsyncTask<GeoFence, Void, Void>() {
        override fun doInBackground(vararg params: GeoFence): Void? {
            geoFenceDao.update(params[0])
            return null
        }
    }

    private class DeleteAsyncTask internal constructor(private val geoFenceDao: GeoFenceDao) :
        AsyncTask<GeoFence, Void, Void>() {
        override fun doInBackground(vararg params: GeoFence): Void? {
            geoFenceDao.delete(params[0])
            return null
        }
    }
}



