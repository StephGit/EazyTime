package ch.bfh.mad.eazytime.geofence.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.geofence.GeoFenceService
import javax.inject.Inject

class BootBroadcastReceiver : BroadcastReceiver() {

    /**
     * Unsafe action-filtering happens in async-task
     */
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult: PendingResult = goAsync()
        val asyncTask = BootReceiverTask(pendingResult, intent)
        asyncTask.execute()
    }

    class BootReceiverTask internal constructor(private val pendingResult: PendingResult, private val intent: Intent) :
        AsyncTask<Void, Void, Void>() {

        @Inject
        lateinit var geoFenceService: GeoFenceService

        init {
            Injector.appComponent.inject(this)
        }

        override fun doInBackground(vararg params: Void?): Void? {
            if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
                // geofences are removed on reboot
                geoFenceService.initGeoFences()
            }
            return null
        }


        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            // Must call finish() so the BroadcastReceiver can be recycled.
            pendingResult.finish()
        }
    }
}