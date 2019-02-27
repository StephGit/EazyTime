package ch.bfh.mad.eazytime.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.geofence.GeofenceErrorMessages
import ch.bfh.mad.eazytime.util.NotificationHandler
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeoFenceReceiver : BroadcastReceiver() {

    private var notificationHandler: NotificationHandler = NotificationHandler()

    val GEOFENCE_ACTION: String = "ch.eazytime.geofence.ACTION_RECEIVE_GEOFENCE";

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceErrorMessages.getErrorString(
                context,
                geofencingEvent.errorCode
            )
            Log.e(TAG, errorMessage)
            return
        }
        handleEvent(context, geofencingEvent)
    }

    private fun handleEvent(context: Context, event: GeofencingEvent) {
        if ((event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) ||
            (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)
        ) {
            val triggeringGeofences = event.triggeringGeofences
            triggeringGeofences.forEach {
                // TODO handle each
                Log.i(TAG, "FancyFenci")
                notificationHandler.sendNotification(context, "FancyFenci")
                var broadcastIntent: Intent = Intent()
                    .setAction(GEOFENCE_ACTION)
                    .putExtra("GEOFENCE_ID", it.requestId)
                    .putExtra("GEOFENCE_TRANSITION_TYPE", event.geofenceTransition)
                LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent)
                // timerService.startDefault/stopDefault
            }
        }
    }
}