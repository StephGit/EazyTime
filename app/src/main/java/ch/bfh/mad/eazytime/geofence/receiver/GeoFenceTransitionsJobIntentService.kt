package ch.bfh.mad.eazytime.geofence.receiver

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.geofence.GeofenceErrorMessages
import ch.bfh.mad.eazytime.remoteViews.homeScreenWidget.WidgetProvider
import ch.bfh.mad.eazytime.remoteViews.notification.NotificationHandler
import ch.bfh.mad.eazytime.util.TimerService
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import javax.inject.Inject

class GeoFenceTransitionsJobIntentService : JobIntentService() {

    @Inject
    lateinit var timerService: TimerService

    @Inject
    lateinit var geoFenceErrorMessages: GeofenceErrorMessages

    @Inject
    lateinit var notificationHandler: NotificationHandler

    init {
        Injector.appComponent.inject(this)
    }

    private val jobId = 111

    fun enqueueWork(context: Context, intent: Intent) {
        enqueueWork(context, GeoFenceTransitionsJobIntentService::class.java, jobId, intent)
    }

    override fun onHandleWork(intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = geoFenceErrorMessages.getErrorString(
                geofencingEvent.errorCode
            )
            Log.e(TAG, errorMessage)
        }
        handleEvent(geofencingEvent)
        applicationContext.sendBroadcast(WidgetProvider.getUpdateAppWidgetsIntent(applicationContext))
    }

    private fun handleEvent(event: GeofencingEvent) {
        val triggeringGeofences = event.triggeringGeofences
        if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            timerService.checkInDefaultProject()
            triggeringGeofences.first().toString()
            Log.d(
                TAG,
                "GeoFenceBroadcastReceiver: checked in default project, Geofence " + triggeringGeofences.first().toString()
            )
            notificationHandler.createEazyTimeNotification()
        } else if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            timerService.checkOut()
            triggeringGeofences.first().toString()
            Log.d(TAG, "GeoFenceBroadcastReceiver: checked out, Geofence " + triggeringGeofences.first().toString())
        }
    }


}