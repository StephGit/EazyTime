package ch.bfh.mad.eazytime.geofence.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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

    @Inject
    lateinit var notificationHandler: NotificationHandler

    @Inject
    lateinit var timerService: TimerService

    init {
        Injector.appComponent.inject(this)
    }


//    private val geofenceAction: String = "ch.eazytime.geofence.ACTION_RECEIVE_GEOFENCE"

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
        val triggeringGeofences = event.triggeringGeofences
        if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            timerService.checkInDefaultProject()
            triggeringGeofences.first().toString()
            Log.d(TAG, "GeoFenceReceiver: checked in default project, Geofence " + triggeringGeofences.first().toString())
            notificationHandler.sendNotification("GeoFenceReceiver: checked in default project, Geofence " + triggeringGeofences.first().toString())
        } else if  (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            timerService.checkOut()
            triggeringGeofences.first().toString()
            Log.d(TAG, "GeoFenceReceiver: checked out, Geofence " + triggeringGeofences.first().toString())
            notificationHandler.sendNotification("GeoFenceReceiver: checked out, Geofence " + triggeringGeofences.first().toString())
        }
    }
}