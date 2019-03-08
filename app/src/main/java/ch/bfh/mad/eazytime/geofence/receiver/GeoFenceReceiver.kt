package ch.bfh.mad.eazytime.geofence.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.geofence.GeofenceErrorMessages
import ch.bfh.mad.eazytime.remoteViews.notification.NotificationHandler
import ch.bfh.mad.eazytime.util.TimerService
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import javax.inject.Inject

class GeoFenceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult: PendingResult = goAsync()
        val asyncTask = GeoFenceReceiverTask(pendingResult, intent)
        asyncTask.execute()
    }

    class GeoFenceReceiverTask internal constructor(
        private val pendingResult: PendingResult,
        private val intent: Intent
    ) : AsyncTask<Void, Void, Void>() {

        private val geoFenceAction: String = "ch.bfh.mad.eazytime.geofence.ACTION_RECEIVE_GEOFENCE"

        @Inject
        lateinit var notificationHandler: NotificationHandler

        @Inject
        lateinit var timerService: TimerService

        @Inject
        lateinit var geoFenceErrorMessages: GeofenceErrorMessages

        init {
            Injector.appComponent.inject(this)
        }

        override fun doInBackground(vararg params: Void?): Void? {
            if (intent.action == geoFenceAction) {
                val geofencingEvent = GeofencingEvent.fromIntent(intent)
                if (geofencingEvent.hasError()) {
                    val errorMessage = geoFenceErrorMessages.getErrorString(
                        geofencingEvent.errorCode
                    )
                    Log.e(TAG, errorMessage)
                    return null
                }
                handleEvent(geofencingEvent)
            }
            return null
        }

        private fun handleEvent(event: GeofencingEvent) {
            val triggeringGeofences = event.triggeringGeofences
            if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                timerService.checkInDefaultProject()
                triggeringGeofences.first().toString()
                Log.d(
                    TAG,
                    "GeoFenceReceiver: checked in default project, Geofence " + triggeringGeofences.first().toString()
                )
                notificationHandler.sendNotification("GeoFenceReceiver: checked in default project, Geofence " + triggeringGeofences.first().toString())
            } else if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                timerService.checkOut()
                triggeringGeofences.first().toString()
                Log.d(TAG, "GeoFenceReceiver: checked out, Geofence " + triggeringGeofences.first().toString())
                notificationHandler.sendNotification("GeoFenceReceiver: checked out, Geofence " + triggeringGeofences.first().toString())
            }
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            // Must call finish() so the BroadcastReceiver can be recycled.
            pendingResult.finish()
        }
    }
}